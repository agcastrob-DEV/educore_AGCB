package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.List;

/**
 * Orquestador de negocio encargado de gobernar las inscripciones y la oferta académica.
 * Conecta los universos de infraestructura, empleados y estudiantes aplicando validaciones rígidas.
 */
public class SeccionController {

  private final Repositorio<Seccion> repoSeccion;
  private final EdificioController edificioController;
  private final Repositorio<Empleado> repoEmpleado;
  private final Repositorio<Estudiante> repoEstudiante;
  private int proximoId = 1;

  /**
   * Construye el controlador inyectando todas las dependencias requeridas para cruzar datos.
   */
  public SeccionController(
      Repositorio<Seccion> repoSeccion,
      EdificioController edificioController,
      Repositorio<Empleado> repoEmpleado,
      Repositorio<Estudiante> repoEstudiante) {
    this.repoSeccion = repoSeccion;
    this.edificioController = edificioController;
    this.repoEmpleado = repoEmpleado;
    this.repoEstudiante = repoEstudiante;
  }

  /**
   * Registra una sección validando la existencia de los recursos y el rol del docente.
   *
   * @throws IllegalArgumentException Si el código está vacío, el aula no existe, o el profesor no es DOCENTE.
   */
  public Seccion registrarSeccion(String codigo, String nombreCurso, int aulaId, int profesorId) throws Exception {
    if (codigo == null || codigo.trim().isEmpty() || nombreCurso == null || nombreCurso.trim().isEmpty()) {
      throw new IllegalArgumentException("El código y nombre de curso son obligatorios.");
    }

    // 1. Validar e Interconectar Aula
    Aula aula = edificioController.buscarAulaPorIdGlobal(aulaId);
    if (aula == null) {
      throw new IllegalArgumentException("No existe un aula en el sistema con el ID: " + aulaId);
    }

    // 2. Validar e Interconectar Profesor
    Empleado profesor = repoEmpleado.buscarPorId(profesorId).orElse(null);
    if (profesor == null) {
      throw new IllegalArgumentException("No existe un empleado en el sistema con el ID: " + profesorId);
    }
    
    // Regla de negocio explícita: Debe ser DOCENTE
    if (profesor.getTipoEmpleado() != TipoEmpleado.DOCENTE) {
      throw new IllegalArgumentException("El empleado asignado debe ser del tipo DOCENTE. Tipo actual: " + profesor.getTipoEmpleado());
    }

    Seccion nueva = new Seccion(proximoId, codigo, nombreCurso, aula, profesor);
    repoSeccion.guardar(nueva);
    proximoId++;
    return nueva;
  }

  /**
   * Inscribe a un estudiante en una sección tras validar su existencia.
   *
   * @throws IllegalArgumentException Si la sección o el estudiante no existen, o si ya está inscrito.
   */
  public void inscribirEstudianteEnSeccion(int seccionId, int estudianteId) throws Exception {
    Seccion seccion = repoSeccion.buscarPorId(seccionId)
        .orElse(null);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe la sección con ID " + seccionId);
    }

    Estudiante estudiante = repoEstudiante.buscarPorId(estudianteId)
        .orElse(null);
    if (estudiante == null) {
      throw new IllegalArgumentException("No existe el estudiante con ID " + estudianteId);
    }

    // Evitar duplicados en el mismo grupo
    for (Estudiante e : seccion.getEstudiantesInscritos()) {
      if (e.getId() == estudianteId) {
        throw new IllegalArgumentException("El estudiante ya se encuentra matriculado en esta sección.");
      }
    }

    seccion.inscribirEstudiante(estudiante);
    repoSeccion.actualizar(seccion); // Llamada crucial exigida por la transición a P2
  }

  /**
   * Remueve a un alumno de una sección específica.
   */
  public void removerEstudianteDeSeccion(int seccionId, int estudianteId) throws Exception {
    Seccion seccion = repoSeccion.buscarPorId(seccionId)
        .orElse(null);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe la sección con ID " + seccionId);
    }

    boolean removido = seccion.desinscribirEstudiante(estudianteId);
    if (!removido) {
      throw new IllegalArgumentException("El estudiante con ID " + estudianteId + " no estaba inscrito en esta sección.");
    }
    repoSeccion.actualizar(seccion);
  }

  public List<Seccion> listarSecciones() throws Exception {
    return repoSeccion.buscarTodos();
  }

  /**
   * Elimina una sección únicamente si no cuenta con alumnos matriculados.
   */
  public void eliminarSeccion(int id) throws Exception {
    Seccion seccion = repoSeccion.buscarPorId(id).orElse(null);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe la sección con ID " + id + ".");
    }

    // Regla de integridad referencial
    if (!seccion.getEstudiantesInscritos().isEmpty()) {
      throw new IllegalArgumentException("No se puede eliminar la sección porque posee estudiantes matriculados.");
    }

    repoSeccion.eliminar(id);
  }
}