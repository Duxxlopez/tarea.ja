package calculo.api.ejercicio.repository;

import calculo.api.ejercicio.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a datos de ventas gestionado por Spring Data JPA. */
public interface VentaRepository extends JpaRepository<Venta, Long> {
}
