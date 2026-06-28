package edu.uam.educore.model.infraestructura;

import edu.uam.educore.enums.TipoAula;

/**
 * Representa un espacio físico identificado por un número dentro de un edificio.
 * Esta clase forma parte de una relación de composición estricta con {@link Edificio};
 * un aula no posee ciclo de vida independiente fuera de su edificio contenedor.
 */
public class Aula {

  private int id;
  private String numero;
  private int capacidad;
  private TipoAula tipo;
  private Edificio edificio;

  /**
   * Construye una nueva instancia de un Aula con sus identificadores técnicos y de negocio.
   *
   * @param id        Clave técnica única a nivel global del sistema, administrada por el controlador.
   * @param numero    Identificador de negocio legible por el usuario (ej: "101", "Lab-02").
   * @param capacidad Cantidad máxima permitida de personas en el recinto.
   * @param tipo      Categoría física del espacio (REGULAR, LABORATORIO, AUDITORIO).
   * @param edificio  Referencia al objeto Edificio al que pertenece por composición.
   */
  public Aula(int id, String numero, int capacidad, TipoAula tipo, Edificio edificio) {
    this.id = id;
    this.numero = numero;
    this.capacidad = capacidad;
    this.tipo = tipo;
    this.edificio = edificio;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public int getCapacidad() {
    return capacidad;
  }

  public void setCapacidad(int capacity) {
    this.capacidad = capacity;
  }

  public TipoAula getTipo() {
    return tipo;
  }

  public void setTipo(TipoAula tipo) {
    this.tipo = tipo;
  }

  /**
   * Obtiene el edificio de pertenencia estructural de esta aula.
   *
   * @return Objeto {@link Edificio} propietario del aula.
   */
  public Edificio getEdificio() {
    return edificio;
  }

  public void setEdificio(Edificio edificio) {
    this.edificio = edificio;
  }

  @Override
  public String toString() {
    return "Aula " + numero + " (" + tipo + ") - Capacidad: " + capacidad;
  }
}