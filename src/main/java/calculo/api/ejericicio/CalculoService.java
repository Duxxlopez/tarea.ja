package calculo.api.ejericicio;

import org.springframework.stereotype.Service;

@Service
public class CalculoService {

    // Calcula el 8% si el valor cumple la condición (S/ 2500 -> 8%)
    public double calcularPorcentaje(double monto) {
        if (monto >= 2500) {
            return monto * 0.08;
        }
        return 0.0;
    }

    // Calcula la bonificación de S/ 300 si las horas extras superan 8
    public double calcularBonificacionHorasExtras(int horasExtras) {
        if (horasExtras > 8) {
            return 300.0;
        }
        return 0.0;
    }
}