package calculo.api.ejercicio;

import calculo.api.ejercicio.dto.request.VentaRequest;
import calculo.api.ejercicio.dto.response.VentaResponse;
import calculo.api.ejercicio.exception.ValidacionException;
import calculo.api.ejercicio.exception.VentaNoEncontradaException;
import calculo.api.ejercicio.repository.VentaRepository;
import calculo.api.ejercicio.service.CalculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class CalculoServiceTests {
    @Autowired
    private CalculoService calculoService;

    @Autowired
    private VentaRepository ventaRepository;

    @BeforeEach
    void limpiarVentas() {
        ventaRepository.deleteAll();
    }

    @Test
    void calculaVentaEnElLimiteSinDescuento() {
        VentaResponse venta = calculoService.calcularYGuardar(solicitud(5, 100, "EFECTIVO"));

        assertEquals(500.00, venta.getSubtotal(), 0.001);
        assertEquals(0.00, venta.getDescuento(), 0.001);
        assertEquals(90.00, venta.getIgv(), 0.001);
        assertEquals(590.00, venta.getTotalFinal(), 0.001);
    }

    @Test
    void aplicaDescuentoAlSuperarElLimite() {
        VentaResponse venta = calculoService.calcularYGuardar(solicitud(1, 501, "EFECTIVO"));

        assertEquals(50.10, venta.getDescuento(), 0.001);
        assertEquals(450.90, venta.getSubtotalConDescuento(), 0.001);
        assertEquals(81.16, venta.getIgv(), 0.001);
        assertEquals(532.06, venta.getTotalFinal(), 0.001);
    }

    @Test
    void agregaComisionCuandoElPagoEsConTarjeta() {
        VentaResponse venta = calculoService.calcularYGuardar(solicitud(1, 501, "tarjeta"));

        assertEquals(15.96, venta.getComisionTarjeta(), 0.001);
        assertEquals(548.02, venta.getTotalFinal(), 0.001);
        assertEquals("TARJETA", venta.getMetodoPago());
    }

    @Test
    void permiteListarBuscarActualizarYEliminar() {
        VentaResponse creada = calculoService.calcularYGuardar(solicitud(1, 100, "EFECTIVO"));
        Long id = creada.getId();

        assertEquals(1, calculoService.listarTodas().size());
        assertEquals(id, calculoService.buscarPorId(id).getId());

        VentaResponse actualizada = calculoService.actualizar(id, solicitud(2, 100, "EFECTIVO"));
        assertEquals(200.00, actualizada.getSubtotal(), 0.001);

        calculoService.eliminar(id);
        assertEquals(0, calculoService.listarTodas().size());
        assertThrows(VentaNoEncontradaException.class, () -> calculoService.buscarPorId(id));
    }

    @Test
    void rechazaDatosInvalidos() {
        assertThrows(ValidacionException.class, () -> calculoService.calcularYGuardar(null));
        assertThrows(ValidacionException.class,
                () -> calculoService.calcularYGuardar(solicitud(0, 100, "EFECTIVO")));
        assertThrows(ValidacionException.class,
                () -> calculoService.calcularYGuardar(solicitud(1, 100, "TRANSFERENCIA")));
    }

    private VentaRequest solicitud(int cantidad, double precio, String metodoPago) {
        VentaRequest request = new VentaRequest();
        request.setProducto("Producto de prueba");
        request.setCantidad(cantidad);
        request.setPrecioUnitario(precio);
        request.setMetodoPago(metodoPago);
        return request;
    }
}
