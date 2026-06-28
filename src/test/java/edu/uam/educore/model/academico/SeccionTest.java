package edu.uam.educore.model.academico;

import static org.junit.jupiter.api.Assertions.*;

import edu.uam.educore.controller.EdificioController;
import edu.uam.educore.controller.SeccionController;
import edu.uam.educore.dao.ListaEdificioRepo;
import edu.uam.educore.dao.ListaEmpleadoRepo;
import edu.uam.educore.dao.ListaEstudianteRepo;
import edu.uam.educore.dao.ListaSeccionRepo;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeccionTest {

  private SeccionController controller;
  private ListaEmpleadoRepo repoEmpleado;
  private ListaEstudianteRepo repoEstudiante;

  @BeforeEach
  void setUp() throws Exception {
    ListaEdificioRepo repoEdificio = new ListaEdificioRepo();
    ListaSeccionRepo repoSeccion = new ListaSeccionRepo();
    repoEmpleado = new ListaEmpleadoRepo();
    repoEstudiante = new ListaEstudianteRepo();

    EdificioController edificioCtrl = new EdificioController(repoEdificio);
    
    // Crear infraestructura base para las pruebas
    edificioCtrl.registrarEdificio("P1", "Pabellón Principal");
    edificioCtrl.registrarAula(1, "Aula 201", 35, TipoAula.REGULAR);

    controller = new SeccionController(repoSeccion, edificioCtrl, repoEmpleado, repoEstudiante);
  }

  @Test
  void registrarSeccion_lanzaExcepcion_siProfesorNoEsDocente() throws Exception {
    // Registrar un empleado que NO es docente (es un GUARDA)
    Empleado guarda = new Empleado(1, "Kevin", "Alfaro", "kevin@uam.edu", 400000.0, TipoEmpleado.GUARDA);
    repoEmpleado.guardar(guarda);

    // Intentar registrar la sección usando el ID de un guarda debe lanzar IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> {
      controller.registrarSeccion("PROG3", "Programación 3", 1001, 1);
    }, "Debió rechazar la sección porque el empleado no es DOCENTE.");
  }

  @Test
  void inscribirEstudiante_lanzaExcepcion_siEstudianteNoExiste() throws Exception {
    Empleado docente = new Empleado(1, "Susana", "Mora", "susa@uam.edu", 700000.0, TipoEmpleado.DOCENTE);
    repoEmpleado.guardar(docente);
    controller.registrarSeccion("PROG3", "Programación 3", 1001, 1);

    // Intentar inscribir un ID de estudiante inexistente (999) debe lanzar una excepción
    assertThrows(IllegalArgumentException.class, () -> {
      controller.inscribirEstudianteEnSeccion(1, 999);
    }, "Debió lanzar excepción debido a que el estudiante no existe.");
  }
}