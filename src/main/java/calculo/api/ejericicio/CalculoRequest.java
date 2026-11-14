package calculo.api.ejercicio;

public class CalculoRequest {
    private double sueldoBase;
    private int horasExtras;

    // Getters y Setters
    public double getSueldoBase() {
        return sueldoBase;
    }
    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
    }
    public int getHorasExtras() {
        return horasExtras;
    }
    public void setHorasExtras(int horasExtras) {
        this.horasExtras = horasExtras;
    }
}