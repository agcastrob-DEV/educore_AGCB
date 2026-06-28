package edu.uam.educore.model.academico;

import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un grupo académico o curso ofertado en un período determinado.
 * Coordina la relación entre un espacio físico ({@link Aula}), un profesor responsable
 * ({@link Empleado} de tipo DOCENTE) y una lista de alumnos inscritos.
 */
public class Seccion {

  private int id;
  private String codigo;
  private String nombreCurso;
  private Aula aula;
  private Empleado profesor;
  private final List<Estudiante> estudiantesInscritos;

  /**
   * Inicializa una nueva sección académica con sus componentes base y una lista vacía de alumnos.
   *
   * @param id          Identificador técnico asignado por el secuenciador automático.
   * @param codigo      Código de negocio de la sección (ej: "PROG3-N1").
   * @param nombreCurso Nombre de la asignatura (ej: "Programación III").
   * @param aula        Instancia del aula asignada para impartir las lecciones.
   * @param profesor    Instancia del empleado docente a cargo del curso.
   */
  public Seccion(int id, String codigo, String nombreCurso, Aula aula, Empleado profesor) {
    this.id = id;
    this.codigo = codigo;
    this.nombreCurso = nombreCurso;
    this.aula = aula;
    this.profesor = profesor;
    this.estudiantesInscritos = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombreCurso() {
    return nombreCurso;
  }

  public void setNombreCurso(String nombreCurso) {
    this.nombreCurso = nombreCurso;
  }

  public Aula getAula() {
    return aula;
  }

  public void setAula(Aula aula) {
    this.aula = aula;
  }

  public Empleado getProfesor() {
    return profesor;
  }

  public void setProfesor(Empleado profesor) {
    this.profesor = profesor;
  }

  /**
   * Obtiene una copia de seguridad de los estudiantes matriculados en esta sección.
   *
   * @return {@link List} con las referencias de los alumnos inscritos.
   */
  public List<Estudiante> getEstudiantesInscritos() {
    return new ArrayList<>(this.estudiantesInscritos);
  }

  /**
   * Agrega un estudiante al grupo una vez superados los filtros del controlador.
   *
   * @param estudiante Estudiante que se unirá a la sección.
   */
  public void inscribirEstudiante(Estudiante estudiante) {
    this.estudiantesInscritos.add(estudiante);
  }

  /**
   * Desvincula a un estudiante del grupo por medio de su ID técnico.
   *
   * @param estudianteId ID del alumno a remover.
   * @return true si el alumno formaba parte de la sección y fue removido con éxito.
   */
  public boolean desinscribirEstudiante(int estudianteId) {
    return this.estudiantesInscritos.removeIf(e -> e.getId() == estudianteId);
  }

  @Override
  public String toString() {
    return String.format(
        "[%s] %s | Profesor: %s | Aula: %s | %d Alumnos",
        codigo, nombreCurso, profesor.getNombre() + " " + profesor.getApellidos(),
        aula.getNumero(), estudiantesInscritos.size());
  }
}