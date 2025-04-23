package com.example.Producto.controller;

import com.example.Producto.dto.ProductoDTO;
import com.example.Producto.service.ProductoService;
import com.example.Producto.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<ProductoDTO>>>> listarComoLista() {
        return service.listarProductos()
                .collectList()
                .map(lista ->
                        ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                                lista.isEmpty()
                                        ? "No se tienen productos registrados"
                                        : "Lista de productos",
                                lista)));
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<ProductoDTO>>> crear(@Valid @RequestBody ProductoDTO dto) {
        return service.crear(dto)
                .map(productoDTO -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Producto creado", productoDTO)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), "Error al crear", null)));
    }

    @GetMapping("producto")
    public Mono<ResponseEntity<ApiResponse<ProductoDTO>>> obtenerPorId(@RequestParam("id") String id) {
        return service.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Producto encontrado", producto)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Producto no encontrado", null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<ProductoDTO>>> actualizar(@RequestParam("id") String id, @Valid @RequestBody ProductoDTO dto) {
        return service.actualizar(id, dto)
                .map(productoDTO -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Producto actualizado", productoDTO)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Producto no encontrado", null)));
    }

    @DeleteMapping
    public Mono<ResponseEntity<ApiResponse<ProductoDTO>>> eliminar(@RequestParam("id") String id) {
        return service.eliminar(id)
                .map(productoDTO -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Producto eliminado", productoDTO)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Producto no encontrado", null)));
    }
}