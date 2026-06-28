package edu.uam.educore.model.personas;

import static org.junit.jupiter.api.Assertions.*;

import edu.uam.educore.enums.TipoEmpleado;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

  @Test
  void creacionEmpleado_asigna_valores_correctos() {
    Empleado e = new Empleado(1, "Andres", "Castro", "andres@uam.edu", 500000.0, TipoEmpleado.DOCENTE);
    assertEquals(1, e.getId());
    assertEquals("Andres", e.getNombre());
    assertEquals(500000.0, e.getSalarioBase(), 0.01);
    assertEquals(TipoEmpleado.DOCENTE, e.getTipoEmpleado());
  }

  @Test
  void getTipo_retorna_texto_esperado() {
    Empleado e = new Empleado(1, "Carlos", "Solano", "carlos@uam.edu", 350000.0, TipoEmpleado.TECNICO);
    assertEquals("Empleado", e.getTipo());
  }

  @Test
  void getInfo_contiene_formato_correcto() {
    Empleado e = new Empleado(1, "Julia", "Flores", "julia@uam.edu", 600000.0, TipoEmpleado.ADMINISTRATIVO);
    String info = e.getInfo();
    assertTrue(info.contains("[Empleado — ADMINISTRATIVO]"));
    assertTrue(info.contains("Julia Flores"));
    assertTrue(info.contains("₡600000"));
  }
}