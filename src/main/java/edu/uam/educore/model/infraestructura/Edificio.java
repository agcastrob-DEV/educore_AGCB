package edu.uam.educore.model.infraestructura;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un pabellón o espacio físico que agrupa diversas aulas de clase.
 * Modela un patrón de composición rígido: las aulas nacen, viven y mueren con el edificio.
 */
public class Edificio {

  private int id;
  private String codigo;
  private String nombre;
  private final List<Aula> aulas;

  /**
   * Inicializa un edificio vacío sin aulas pre-asignadas.
   *
   * @param id     Clave técnica incremental para la administración en la persistencia.
   * @param codigo Identificador único de negocio para el usuario (ej: "A", "Pabellón B").
   * @param nombre Nombre descriptivo de la estructura física.
   */
  public Edificio(int id, String codigo, String nombre) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.aulas = new ArrayList<>();
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

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Obtiene una copia segura de la lista de aulas para cumplir con la encapsulación.
   *
   * @return {@link List} que contiene las aulas asociadas a la infraestructura.
   */
  public List<Aula> getAulas() {
    return new ArrayList<>(this.aulas);
  }

  /**
   * Agrega un aula directamente a la colección de la estructura edilicia.
   *
   * @param aula Objeto {@link Aula} completamente validado para ser alojado.
   */
  public void agregarAula(Aula aula) {
    this.aulas.add(aula);
  }

  /**
   * Remueve un aula interna del edificio basándose en su ID técnico unificado.
   *
   * @param aulaId ID numérico del aula a remover.
   * @return true si el aula existía y se eliminó; de lo contrario false.
   */
  public boolean removerAula(int aulaId) {
    return this.aulas.removeIf(aula -> aula.getId() == aulaId);
  }

  @Override
  public String toString() {
    return "[" + codigo + "] " + nombre + " (" + aulas.size() + " aulas asignadas)";
  }
}