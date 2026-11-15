package calculo.api.ejericicio;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Almacenamiento en memoria separado de las reglas de cálculo. */
@Repository
public class VentaRepository {
    private final ConcurrentHashMap<Long, VentaResponse> ventas = new ConcurrentHashMap<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    public VentaResponse guardar(VentaResponse venta) {
        if (venta.getId() == null) {
            venta.setId(siguienteId.getAndIncrement());
        }
        ventas.put(venta.getId(), venta);
        return venta;
    }

    public List<VentaResponse> listarTodas() {
        return ventas.values().stream()
                .sorted(Comparator.comparing(VentaResponse::getId))
                .toList();
    }

    public Optional<VentaResponse> buscarPorId(Long id) {
        return Optional.ofNullable(ventas.get(id));
    }

    public boolean eliminar(Long id) {
        return ventas.remove(id) != null;
    }
}
