package com.example.Producto.exception;

import com.example.Producto.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleBindException(
            WebExchangeBindException ex) {

        String errores = ex.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiResponse<Void> resp = new ApiResponse<>(
                400,
                "Errores de validación: " + errores,
                null
        );
        return Mono.just(ResponseEntity.badRequest().body(resp));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleInputException(
            ServerWebInputException ex) {
        ApiResponse<Void> resp = new ApiResponse<>(
                400,
                "Entrada inválida: " + ex.getReason(),
                null
        );
        return Mono.just(ResponseEntity.badRequest().body(resp));
    }
}
