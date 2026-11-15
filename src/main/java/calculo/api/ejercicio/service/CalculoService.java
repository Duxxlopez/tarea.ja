package calculo.api.ejercicio.service;

import calculo.api.ejercicio.dto.request.VentaRequest;
import calculo.api.ejercicio.dto.request.VentaPatchRequest;
import calculo.api.ejercicio.dto.response.VentaResponse;
import calculo.api.ejercicio.exception.ValidacionException;
import calculo.api.ejercicio.exception.VentaNoEncontradaException;
import calculo.api.ejercicio.model.Venta;
import calculo.api.ejercicio.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CalculoService {
    private static final BigDecimal LIMITE_DESCUENTO = new BigDecimal("500.00");
    private static final BigDecimal TASA_DESCUENTO = new BigDecimal("0.10");
    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");
    private static final BigDecimal TASA_TARJETA = new BigDecimal("0.03");
    private static final BigDecimal MAX_SUBTOTAL = new BigDecimal("99999999999999999.99");

    private final VentaRepository ventaRepository;

    public CalculoService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public VentaResponse calcularYGuardar(VentaRequest request) {
        Venta venta = ventaRepository.save(calcular(request, null));
        return convertirARespuesta(venta);
    }

    public List<VentaResponse> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirARespuesta)
                .toList();
    }

    public VentaResponse buscarPorId(Long id) {
        validarId(id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNoEncontradaException(id));
        return convertirARespuesta(venta);
    }

    public VentaResponse actualizar(Long id, VentaRequest request) {
        validarId(id);
        if (ventaRepository.findById(id).isEmpty()) {
            throw new VentaNoEncontradaException(id);
        }
        Venta venta = ventaRepository.save(calcular(request, id));
        return convertirARespuesta(venta);
    }

    public VentaResponse modificarParcialmente(Long id, VentaPatchRequest request) {
        validarId(id);
        if (request == null || (request.getProducto() == null && request.getMetodoPago() == null)) {
            throw new ValidacionException("Envía al menos producto o metodoPago para modificar la venta.");
        }

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNoEncontradaException(id));

        if (request.getProducto() != null) {
            String producto = request.getProducto().trim();
            if (producto.isEmpty()) {
                throw new ValidacionException("El nombre del producto es obligatorio.");
            }
            if (producto.length() > 150) {
                throw new ValidacionException("El nombre del producto no puede superar 150 caracteres.");
            }
            venta.setProducto(producto);
        }

        if (request.getMetodoPago() != null) {
            String metodoPago = request.getMetodoPago().trim().toUpperCase();
            if (!metodoPago.equals("TARJETA") && !metodoPago.equals("EFECTIVO")) {
                throw new ValidacionException("El método de pago solo puede ser TARJETA o EFECTIVO.");
            }
            venta.setMetodoPago(metodoPago);
            BigDecimal base = venta.getSubtotalConDescuento().add(venta.getIgv());
            BigDecimal comision = metodoPago.equals("TARJETA")
                    ? redondear(base.multiply(TASA_TARJETA)) : BigDecimal.ZERO;
            venta.setComisionTarjeta(comision);
            venta.setTotalFinal(redondear(base.add(comision)));
        }

        return convertirARespuesta(ventaRepository.save(venta));
    }

    public void eliminar(Long id) {
        validarId(id);
        if (!ventaRepository.existsById(id)) {
            throw new VentaNoEncontradaException(id);
        }
        ventaRepository.deleteById(id);
    }

    private Venta calcular(VentaRequest request, Long id) {
        validar(request);

        BigDecimal subtotal = BigDecimal.valueOf(request.getCantidad())
                .multiply(BigDecimal.valueOf(request.getPrecioUnitario()));
        if (subtotal.compareTo(MAX_SUBTOTAL) > 0) {
            throw new ValidacionException("El monto de la venta excede el límite permitido.");
        }
        BigDecimal descuento = subtotal.compareTo(LIMITE_DESCUENTO) > 0
                ? subtotal.multiply(TASA_DESCUENTO) : BigDecimal.ZERO;
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal igv = subtotalConDescuento.multiply(TASA_IGV);
        BigDecimal totalConIgv = subtotalConDescuento.add(igv);
        String metodoPago = request.getMetodoPago().trim().toUpperCase();
        BigDecimal comisionTarjeta = "TARJETA".equals(metodoPago)
                ? totalConIgv.multiply(TASA_TARJETA) : BigDecimal.ZERO;

        Venta venta = new Venta();
        venta.setId(id);
        venta.setProducto(request.getProducto().trim());
        venta.setSubtotal(redondear(subtotal));
        venta.setDescuento(redondear(descuento));
        venta.setSubtotalConDescuento(redondear(subtotalConDescuento));
        venta.setIgv(redondear(igv));
        venta.setComisionTarjeta(redondear(comisionTarjeta));
        venta.setTotalFinal(redondear(totalConIgv.add(comisionTarjeta)));
        venta.setMetodoPago(metodoPago);
        return venta;
    }

    private void validar(VentaRequest request) {
        if (request == null) {
            throw new ValidacionException("El cuerpo de la solicitud es obligatorio.");
        }
        if (request.getProducto() == null || request.getProducto().trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto es obligatorio.");
        }
        if (request.getProducto().trim().length() > 150) {
            throw new ValidacionException("El nombre del producto no puede superar 150 caracteres.");
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

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException("El ID debe ser mayor a 0.");
        }
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private VentaResponse convertirARespuesta(Venta venta) {
        VentaResponse respuesta = new VentaResponse();
        respuesta.setId(venta.getId());
        respuesta.setProducto(venta.getProducto());
        respuesta.setSubtotal(venta.getSubtotal().doubleValue());
        respuesta.setDescuento(venta.getDescuento().doubleValue());
        respuesta.setSubtotalConDescuento(venta.getSubtotalConDescuento().doubleValue());
        respuesta.setIgv(venta.getIgv().doubleValue());
        respuesta.setComisionTarjeta(venta.getComisionTarjeta().doubleValue());
        respuesta.setTotalFinal(venta.getTotalFinal().doubleValue());
        respuesta.setMetodoPago(venta.getMetodoPago());
        return respuesta;
    }
}
