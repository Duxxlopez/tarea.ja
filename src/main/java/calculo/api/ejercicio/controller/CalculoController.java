package calculo.api.ejercicio.controller;

import calculo.api.ejercicio.dto.request.VentaRequest;
import calculo.api.ejercicio.dto.response.VentaResponse;
import calculo.api.ejercicio.service.CalculoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class CalculoController {
    private final CalculoService calculoService;

    public CalculoController(CalculoService calculoService) {
        this.calculoService = calculoService;
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarVentas() {
        return ResponseEntity.ok(calculoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(calculoService.buscarPorId(id));
    }

    @PostMapping("/calcular")
    public ResponseEntity<VentaResponse> calcularVenta(@RequestBody(required = false) VentaRequest request) {
        VentaResponse venta = calculoService.calcularYGuardar(request);
        return ResponseEntity.created(URI.create("/api/ventas/" + venta.getId())).body(venta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponse> actualizarVenta(
            @PathVariable Long id,
            @RequestBody(required = false) VentaRequest request) {
        return ResponseEntity.ok(calculoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarVenta(@PathVariable Long id) {
        calculoService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Venta eliminada correctamente con ID: " + id));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> manejarHead() {
        return ResponseEntity.ok()
                .header("X-Info-Api", "API Bodega Don Pepe Activa")
                .build();
    }
}
