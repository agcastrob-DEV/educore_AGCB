package edu.uam.educore.model.infraestructura;

import static org.junit.jupiter.api.Assertions.*;

import edu.uam.educore.enums.TipoAula;
import org.junit.jupiter.api.Test;

/**
 * Suite de pruebas unitarias encargada de blindar los requisitos de composición.
 * Asegura la integridad referencial bidireccional entre la estructura y sus recintos.
 */
class EdificioAulaTest {

  @Test
  void composicionEdificioAula_vincula_y_encapsula_correctamente() {
    // 1. Configuración del escenario inicial
    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(101, "Lab-01", 30, TipoAula.LABORATORIO, edificio);
    
    // 2. Ejecución del estímulo lógico
    edificio.agregarAula(aula);
    
    // 3. Verificaciones de la composición
    assertEquals(1, edificio.getAulas().size(), "El edificio debe registrar exactamente un aula interna.");
    assertEquals("Lab-01", edificio.getAulas().get(0).getNumero(), "El número del aula recuperada debe coincidir.");
    assertEquals(edificio, aula.getEdificio(), "El aula debe conocer explícitamente la instancia del edificio contenedor.");
  }
}