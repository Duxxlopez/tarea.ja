package calculo.api.ejericicio;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calculo")
public class CalculoController {

    private final CalculoService calculoService;

    public CalculoController(CalculoService calculoService) {
        this.calculoService = calculoService;
    }

    // Endpoint GET: Prueba rápida de verificación
    @GetMapping("/ping")
    public String status() {
        return "API Calculo activa";
    }

    // Endpoint POST: Recibe los datos y retorna el cálculo
    @PostMapping("/procesar")
    public Map<String, Object> procesarCalculo(@RequestBody Map<String, Object> datos) {
        double monto = Double.parseDouble(datos.getOrDefault("monto", 0).toString());
        int horasExtras = Integer.parseInt(datos.getOrDefault("horasExtras", 0).toString());

        double retencion = calculoService.calcularPorcentaje(monto);
        double bonificacion = calculoService.calcularBonificacionHorasExtras(horasExtras);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("montoIngresado", monto);
        respuesta.put("retencion8", retencion);
        respuesta.put("horasExtras", horasExtras);
        respuesta.put("bonificacion", bonificacion);
        respuesta.put("totalFinal", monto - retencion + bonificacion);

        return respuesta;
    }
}