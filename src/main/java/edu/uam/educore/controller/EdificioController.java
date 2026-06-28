package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador encargado de centralizar la lógica de negocio del módulo de infraestructura.
 * Administra el ciclo de vida de los edificios y gobierna la composición de las aulas
 * mediante un secuenciador global único de identificadores técnicos.
 */
public class EdificioController {

  private final Repositorio<Edificio> repo;
  private int proximoEdificioId = 1;
  private int proximoAulaId = 1001; // Contador global único para las aulas del sistema

  /**
   * Construye el controlador asociándole su repositorio de persistencia.
   *
   * @param repo Repositorio compartido de edificios.
   */
  public EdificioController(Repositorio<Edificio> repo) {
    this.repo = repo;
  }

  // ── LÓGICA DE EDIFICIOS ───────────────────────────────────────────────────

  /**
   * Registra un nuevo edificio en el sistema de manera limpia.
   *
   * @param codigo Código identificador de negocio (ej: "CEL", "PAB-A").
   * @param nombre Nombre descriptivo del inmueble.
   * @return El objeto {@link Edificio} creado y persistido.
   * @throws IllegalArgumentException Si los datos violan las reglas de validación.
   */
  public Edificio registrarEdificio(String codigo, String nombre) throws Exception {
    validarTexto(codigo, "El código del edificio es obligatorio.");
    validarTexto(nombre, "El nombre del edificio es obligatorio.");

    Edificio nuevo = new Edificio(proximoEdificioId, codigo, nombre);
    repo.guardar(nuevo);
    proximoEdificioId++;
    return nuevo;
  }

  /**
   * Devuelve la lista completa de edificios registrados.
   */
  public List<Edificio> listarEdificios() throws Exception {
    return repo.buscarTodos();
  }

  /**
   * Busca un edificio específico por su ID técnico.
   */
  public Edificio buscarEdificioPorId(int id) throws Exception {
    return repo.buscarPorId(id).orElse(null);
  }

  /**
   * Elimina un edificio del repositorio únicamente si cumple las restricciones de integridad.
   *
   * @param id ID del edificio a dar de baja.
   * @throws IllegalArgumentException Si el edificio no existe o si posee aulas compuestas activas.
   */
  public void eliminarEdificio(int id) throws Exception {
    Edificio edificio = buscarEdificioPorId(id);
    if (edificio == null) {
      throw new IllegalArgumentException("No existe un edificio con el ID " + id + ".");
    }
    
    // Regla de integridad referencial del enunciado: No borrar si tiene aulas
    if (!edificio.getAulas().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar el edificio '" + edificio.getNombre() 
          + "' porque contiene " + edificio.getAulas().size() + " aula(s) asignada(s).");
    }
    
    repo.eliminar(id);
  }

  // ── LÓGICA DE AULAS (COMPOSICIÓN EXTRAÍDA DESDE EDIFICIO) ─────────────────

  /**
   * Crea un aula física y la aloja directamente dentro del edificio correspondiente.
   * Al no poseer un DAO propio, el aula se guarda actualizando el estado raíz del Edificio.
   *
   * @param edificioId ID del edificio contenedor.
   * @param numero     Número o identificador del aula (ej: "402").
   * @param capacidad  Capacidad máxima de alumnos.
   * @param tipo       Tipo de entorno (REGULAR, LABORATORIO, AUDITORIO).
   * @return El objeto {@link Aula} instanciado.
   * @throws IllegalArgumentException Si el edificio no existe o si los datos del aula son erróneos.
   */
  public Aula registrarAula(int edificioId, String numero, int capacidad, TipoAula tipo) throws Exception {
    Edificio edificio = buscarEdificioPorId(edificioId);
    if (edificio == null) {
      throw new IllegalArgumentException("No existe el edificio con ID " + edificioId + " para asociar el aula.");
    }

    validarTexto(numero, "El número de aula es obligatorio.");
    if (capacidad <= 0) {
      throw new IllegalArgumentException("La capacidad del aula debe ser un número entero positivo mayor a cero.");
    }
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo de aula es mandatorio.");
    }

    // Generación con el ID autoincremental unificado globalmente
    Aula nuevaAula = new Aula(proximoAulaId, numero, capacidad, tipo, edificio);
    edificio.agregarAula(nuevaAula);
    
    // Guardamos los cambios estructurales en el repositorio del agregado raíz
    repo.actualizar(edificio);
    proximoAulaId++;
    
    return nuevaAula;
  }

  /**
   * Realiza un escaneo transversal sobre todos los edificios para localizar un aula por su ID global.
   *
   * @param aulaId ID único global del aula a buscar.
   * @return Objeto {@link Aula} si se encuentra, de lo contrario null.
   */
  public Aula buscarAulaPorIdGlobal(int aulaId) throws Exception {
    for (Edificio e : repo.buscarTodos()) {
      for (Aula a : e.getAulas()) {
        if (a.getId() == aulaId) {
          return a;
        }
      }
    }
    return null;
  }

  /**
   * Recopila todas las aulas del sistema sin importar a qué edificio pertenezcan.
   */
  public List<Aula> listarTodasLasAulas() throws Exception {
    List<Aula> todas = new ArrayList<>();
    for (Edificio e : repo.buscarTodos()) {
      todas.addAll(e.getAulas());
    }
    return todas;
  }

  // ── HELPERS INTERNOS ──────────────────────────────────────────────────────

  private void validarTexto(String texto, String mensajeError) {
    if (texto == null || texto.trim().isEmpty()) {
      throw new IllegalArgumentException(mensajeError);
    }
  }
}