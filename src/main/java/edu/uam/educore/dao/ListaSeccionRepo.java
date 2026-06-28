package edu.uam.educore.dao;

import edu.uam.educore.model.academico.Seccion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Almacén en memoria encargado de centralizar el almacenamiento persistente de las Secciones.
 * Cumple con el contrato genérico definido por la arquitectura base del sistema.
 */
public class ListaSeccionRepo extends Repositorio<Seccion> {

  private final List<Seccion> lista = new ArrayList<>();

  @Override
  public void guardar(Seccion s) {
    lista.add(s);
  }

  @Override
  public void actualizar(Seccion actualizado) {
    for (int i = 0; i < lista.size(); i++) {
      if (lista.get(i).getId() == actualizado.getId()) {
        lista.set(i, actualizado);
        return;
      }
    }
  }

  @Override
  public void eliminar(int id) {
    for (int i = 0; i < lista.size(); i++) {
      if (lista.get(i).getId() == id) {
        lista.remove(i);
        return;
      }
    }
  }

  @Override
  public Optional<Seccion> buscarPorId(int id) {
    for (Seccion s : lista) {
      if (s.getId() == id) {
        return Optional.of(s);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Seccion> buscarTodos() {
    return new ArrayList<>(lista);
  }
}