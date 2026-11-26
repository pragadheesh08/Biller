
package org.biller.common.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(java.util.stream.Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage(),
                (first, second) -> first
            ));

        return ResponseEntity.badRequest().body(
            Map.of(
                "status", "error",
                "timestamp", Instant.now(),
                "errors", errors
            )
        );
    }
}
