package calculo.api.ejercicio;

public class CalculoResponse {
    private double sueldoBase;
    private double bonificacionHorasExtras;
    private double descuentoPorcentaje;
    private double valorNeto;

    public CalculoResponse(double sueldoBase, double bonificacionHorasExtras, double descuentoPorcentaje, double valorNeto) {
        this.sueldoBase = sueldoBase;
        this.bonificacionHorasExtras = bonificacionHorasExtras;
        this.descuentoPorcentaje = descuentoPorcentaje;
        this.valorNeto = valorNeto;
    }

    // Getters y Setters
    public double getSueldoBase() { return sueldoBase; }
    public double getBonificacionHorasExtras() { return bonificacionHorasExtras; }
    public double getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public double getValorNeto() { return valorNeto; }
}