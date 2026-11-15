package calculo.api.ejercicio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** Datos de una venta calculada. */
@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String producto;

    @Column(nullable = false, precision = 21, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 21, scale = 2)
    private BigDecimal descuento;

    @Column(name = "subtotal_con_descuento", nullable = false, precision = 21, scale = 2)
    private BigDecimal subtotalConDescuento;

    @Column(nullable = false, precision = 21, scale = 2)
    private BigDecimal igv;

    @Column(name = "comision_tarjeta", nullable = false, precision = 21, scale = 2)
    private BigDecimal comisionTarjeta;

    @Column(name = "total_final", nullable = false, precision = 21, scale = 2)
    private BigDecimal totalFinal;

    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago;

}
