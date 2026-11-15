package calculo.api.ejericicio;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CalculoService {
    private static final BigDecimal LIMITE_DESCUENTO = new BigDecimal("500.00");
    private static final BigDecimal TASA_DESCUENTO = new BigDecimal("0.10");
    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");
    private static final BigDecimal TASA_TARJETA = new BigDecimal("0.03");

    private final VentaRepository ventaRepository;

    public CalculoService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public VentaResponse calcularYGuardar(VentaRequest request) {
        return ventaRepository.guardar(calcular(request, null));
    }

    public List<VentaResponse> listarTodas() {
        return ventaRepository.listarTodas();
    }

    public Optional<VentaResponse> buscarPorId(Long id) {
        return ventaRepository.buscarPorId(id);
    }

    public VentaResponse actualizar(Long id, VentaRequest request) {
        if (id == null || id <= 0) {
            throw new ValidacionException("El ID debe ser mayor a 0.");
        }
        if (ventaRepository.buscarPorId(id).isEmpty()) {
            throw new VentaNoEncontradaException(id);
        }
        return ventaRepository.guardar(calcular(request, id));
    }

    public boolean eliminar(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException("El ID debe ser mayor a 0.");
        }
        return ventaRepository.eliminar(id);
    }

    private VentaResponse calcular(VentaRequest request, Long id) {
        validar(request);

        BigDecimal cantidad = BigDecimal.valueOf(request.getCantidad());
        BigDecimal precioUnitario = BigDecimal.valueOf(request.getPrecioUnitario());
        BigDecimal subtotal = cantidad.multiply(precioUnitario);
        BigDecimal descuento = subtotal.compareTo(LIMITE_DESCUENTO) > 0
                ? subtotal.multiply(TASA_DESCUENTO) : BigDecimal.ZERO;
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal igv = subtotalConDescuento.multiply(TASA_IGV);
        BigDecimal totalConIgv = subtotalConDescuento.add(igv);
        boolean pagaConTarjeta = "TARJETA".equals(request.getMetodoPago().trim().toUpperCase());
        BigDecimal comisionTarjeta = pagaConTarjeta
                ? totalConIgv.multiply(TASA_TARJETA) : BigDecimal.ZERO;

        VentaResponse venta = new VentaResponse();
        venta.setId(id);
        venta.setProducto(request.getProducto().trim());
        venta.setSubtotal(redondear(subtotal));
        venta.setDescuento(redondear(descuento));
        venta.setSubtotalConDescuento(redondear(subtotalConDescuento));
        venta.setIgv(redondear(igv));
        venta.setComisionTarjeta(redondear(comisionTarjeta));
        venta.setTotalFinal(redondear(totalConIgv.add(comisionTarjeta)));
        venta.setMetodoPago(request.getMetodoPago().trim().toUpperCase());
        return venta;
    }

    private void validar(VentaRequest request) {
        if (request == null) {
            throw new ValidacionException("El cuerpo de la solicitud es obligatorio.");
        }
        if (request.getProducto() == null || request.getProducto().trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto es obligatorio.");
        }
        if (request.getCantidad() <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor a 0.");
        }
        if (!Double.isFinite(request.getPrecioUnitario()) || request.getPrecioUnitario() <= 0) {
            throw new ValidacionException("El precio unitario debe ser un número mayor a 0.");
        }
        String metodoPago = request.getMetodoPago();
        if (metodoPago == null ||
                !(metodoPago.trim().equalsIgnoreCase("TARJETA") ||
                        metodoPago.trim().equalsIgnoreCase("EFECTIVO"))) {
            throw new ValidacionException("El método de pago solo puede ser TARJETA o EFECTIVO.");
        }
    }

    private double redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
