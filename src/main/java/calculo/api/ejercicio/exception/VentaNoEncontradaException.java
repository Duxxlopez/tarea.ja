package calculo.api.ejercicio.exception;

public class VentaNoEncontradaException extends RuntimeException {
    public VentaNoEncontradaException(Long id) {
        super("Venta no encontrada con ID: " + id);
    }
}
