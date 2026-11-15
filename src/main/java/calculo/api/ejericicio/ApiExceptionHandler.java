package calculo.api.ejericicio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(ValidacionException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(VentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrada(VentaNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }
}
