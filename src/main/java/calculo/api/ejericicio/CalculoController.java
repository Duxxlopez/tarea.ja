package calculo.api.ejericicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class CalculoController {

    private final CalculoService calculoService;

    public CalculoController(CalculoService calculoService) {
        this.calculoService = calculoService;
    }

    // 1. GET: Listar todas las ventas registradas
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarVentas() {
        return ResponseEntity.ok(calculoService.listarTodas());
    }

    // 2. GET por ID: Consultar una venta específica (ej. /api/ventas/1)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable Long id) {
        return calculoService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Venta no encontrada")));
    }

    // 3. POST: Calcular y registrar una nueva venta (/api/ventas/calcular)
    @PostMapping("/calcular")
    public ResponseEntity<?> calcularVenta(@RequestBody VentaRequest request) {
        try {
            VentaResponse response = calculoService.calcularYGuardar(request);
            return ResponseEntity.ok(response);
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. PUT: Actualizar una venta existente por ID (ej. /api/ventas/1)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody VentaRequest request) {
        try {
            VentaResponse response = calculoService.actualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 5. DELETE: Eliminar una venta por ID (ej. /api/ventas/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        boolean eliminado = calculoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok(Map.of("mensaje", "Venta eliminada correctamente con ID: " + id));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "No se encontró la venta para eliminar"));
        }
    }
    // 6. HEAD: Retorna solo las cabeceras y el estado (sin cuerpo) para verificar la API
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<?> manejarHead() {
        return ResponseEntity.ok()
                .header("X-Info-Api", "API Bodega Don Pepe Activa")
                .build();
    }
}
