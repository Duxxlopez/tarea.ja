package calculo.api.ejericicio;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CalculoService {
    private final List<VentaResponse> ventasDB = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    public VentaResponse calcularYGuardar(VentaRequest request) {
        validar(request);

        double subtotal = request.getCantidad() * request.getPrecioUnitario();
        double descuento = subtotal > 500.0 ? subtotal * 0.10 : 0.0;
        double subtotalConDescuento = subtotal - descuento;
        double igv = subtotalConDescuento * 0.18;
        double totalConIgv = subtotalConDescuento + igv;
        double comisionTarjeta = request.getMetodoPago().equalsIgnoreCase("TARJETA") ? totalConIgv * 0.03 : 0.0;
        double totalFinal = totalConIgv + comisionTarjeta;

        VentaResponse response = new VentaResponse();
        response.setId(contadorId.getAndIncrement());
        response.setProducto(request.getProducto());
        response.setSubtotal(redondear(subtotal));
        response.setDescuento(redondear(descuento));
        response.setSubtotalConDescuento(redondear(subtotalConDescuento));
        response.setIgv(redondear(igv));
        response.setComisionTarjeta(redondear(comisionTarjeta));
        response.setTotalFinal(redondear(totalFinal));
        response.setMetodoPago(request.getMetodoPago().toUpperCase());

        ventasDB.add(response);
        return response;
    }

    public List<VentaResponse> listarTodas() {
        return ventasDB;
    }

    public Optional<VentaResponse> buscarPorId(Long id) {
        return ventasDB.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    public VentaResponse actualizar(Long id, VentaRequest request) {
        VentaResponse existente = buscarPorId(id)
                .orElseThrow(() -> new ValidacionException("Venta no encontrada con ID: " + id));

        validar(request);
        double subtotal = request.getCantidad() * request.getPrecioUnitario();
        double descuento = subtotal > 500.0 ? subtotal * 0.10 : 0.0;
        double subtotalConDescuento = subtotal - descuento;
        double igv = subtotalConDescuento * 0.18;
        double totalConIgv = subtotalConDescuento + igv;
        double comisionTarjeta = request.getMetodoPago().equalsIgnoreCase("TARJETA") ? totalConIgv * 0.03 : 0.0;
        double totalFinal = totalConIgv + comisionTarjeta;

        existente.setProducto(request.getProducto());
        existente.setSubtotal(redondear(subtotal));
        existente.setDescuento(redondear(descuento));
        existente.setSubtotalConDescuento(redondear(subtotalConDescuento));
        existente.setIgv(redondear(igv));
        existente.setComisionTarjeta(redondear(comisionTarjeta));
        existente.setTotalFinal(redondear(totalFinal));
        existente.setMetodoPago(request.getMetodoPago().toUpperCase());

        return existente;
    }

    public boolean eliminar(Long id) {
        return ventasDB.removeIf(v -> v.getId().equals(id));
    }

    private void validar(VentaRequest request) {
        if (request.getProducto() == null || request.getProducto().trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto no puede ser nulo ni vacío.");
        }
        if (request.getCantidad() <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor a 0.");
        }
        if (request.getPrecioUnitario() <= 0) {
            throw new ValidacionException("El precio unitario debe ser mayor a 0.");
        }
        if (request.getMetodoPago() == null ||
                (!request.getMetodoPago().equalsIgnoreCase("TARJETA") &&
                        !request.getMetodoPago().equalsIgnoreCase("EFECTIVO"))) {
            throw new ValidacionException("El método de pago solo puede ser TARJETA o EFECTIVO.");
        }
    }

    private double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}