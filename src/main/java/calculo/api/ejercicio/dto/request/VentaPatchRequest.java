package calculo.api.ejercicio.dto.request;

/** Campos que se pueden modificar parcialmente en una venta existente. */
public class VentaPatchRequest {
    private String producto;
    private String metodoPago;

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
