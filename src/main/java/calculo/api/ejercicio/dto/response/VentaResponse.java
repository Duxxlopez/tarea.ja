package calculo.api.ejercicio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
