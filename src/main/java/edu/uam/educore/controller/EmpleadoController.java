package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.personas.Empleado;
import java.util.List;
import java.util.Optional;

public class EmpleadoController {

  private final Repositorio<Empleado> repo;
  private int proximoId = 1;

  public EmpleadoController(Repositorio<Empleado> repo) {
    this.repo = repo;
  }

  public Empleado registrar(String nombre, String apellidos, String email, double salarioBase, TipoEmpleado tipo) throws Exception {
    validarBase(nombre, apellidos, email, salarioBase, tipo);
    Empleado e = new Empleado(proximoId, nombre, apellidos, email, salarioBase, tipo);
    repo.guardar(e);
    proximoId++;
    return e;
  }

  public List<Empleado> listar() throws Exception {
    return repo.buscarTodos();
  }

  public Empleado buscarPorId(int id) throws Exception {
    Optional<Empleado> resultado = repo.buscarPorId(id);
    return resultado.orElse(null);
  }

  public Empleado actualizar(int id, String nombre, String apellidos, String email, double salarioBase, TipoEmpleado tipo) throws Exception {
    Empleado e = buscarPorId(id);
    if (e == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + id + ".");
    }
    validarBase(nombre, apellidos, email, salarioBase, tipo);
    e.setNombre(nombre);
    e.setApellidos(apellidos);
    e.setEmail(email);
    e.setSalarioBase(salarioBase);
    e.setTipoEmpleado(tipo);
    repo.actualizar(e);
    return e;
  }

  public void eliminar(int id) throws Exception {
    Empleado e = buscarPorId(id);
    if (e == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + id + ".");
    }
    repo.eliminar(id);
  }

  private void validarBase(String nombre, String apellidos, String email, double salarioBase, TipoEmpleado tipo) {
    if (nombre == null || nombre.isEmpty() || apellidos == null || apellidos.isEmpty()) {
      throw new IllegalArgumentException("El nombre y los apellidos son obligatorios.");
    }
    if (email == null || email.isEmpty() || !email.contains("@")) {
      throw new IllegalArgumentException("El correo electrónico no es válido.");
    }
    if (salarioBase < 0) {
      throw new IllegalArgumentException("El salario base no puede ser un número negativo.");
    }
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo de empleado es obligatorio.");
    }
  }
}