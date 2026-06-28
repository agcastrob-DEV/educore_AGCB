package edu.uam.educore.model.personas;

import edu.uam.educore.enums.TipoEmpleado;

public class Empleado extends Persona {

  private double salarioBase;
  private TipoEmpleado tipoEmpleado;

  public Empleado(int id, String nombre, String apellidos, String email, double salarioBase, TipoEmpleado tipoEmpleado) {
    super(id, nombre, apellidos, email);
    this.salarioBase = salarioBase;
    this.tipoEmpleado = tipoEmpleado;
  }

  public double getSalarioBase() {
    return salarioBase;
  }

  public void setSalarioBase(double salarioBase) {
    this.salarioBase = salarioBase;
  }

  public TipoEmpleado getTipoEmpleado() {
    return tipoEmpleado;
  }

  public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
    this.tipoEmpleado = tipoEmpleado;
  }

  @Override
  public String getTipo() {
    return "Empleado";
  }

  @Override
  public String getInfo() {
    return String.format(
        "[%s — %s] %s %s | Salario Base: ₡%.2f",
        getTipo(), tipoEmpleado.name(), getNombre(), getApellidos(), salarioBase);
  }
}