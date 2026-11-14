package calculo.api.ejericicio;

public class VentaResponse {
    private Long id;
    private String producto;
    private double subtotal;
    private double descuento;
    private double subtotalConDescuento;
    private double igv;
    private double comisionTarjeta;
    private double totalFinal;
    private String metodoPago;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getSubtotalConDescuento() { return subtotalConDescuento; }
    public void setSubtotalConDescuento(double subtotalConDescuento) { this.subtotalConDescuento = subtotalConDescuento; }

    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }

    public double getComisionTarjeta() { return comisionTarjeta; }
    public void setComisionTarjeta(double comisionTarjeta) { this.comisionTarjeta = comisionTarjeta; }

    public double getTotalFinal() { return totalFinal; }
    public void setTotalFinal(double totalFinal) { this.totalFinal = totalFinal; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}