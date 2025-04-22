package com.example.Producto.service;

import com.example.Producto.dto.ProductoDTO;
import com.example.Producto.entity.Producto;
import com.example.Producto.mapper.ProductoMapper;
import com.example.Producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;

    private final ProductoMapper productoMapper;

    public Flux<ProductoDTO> listarProductos() {
        return repository.findAll().map(productoMapper::toDTO);
    }

    public Mono<ProductoDTO> obtenerPorId(String id) {
        return repository.findById(id).map(productoMapper::toDTO);
    }

    public Mono<ProductoDTO> crear(ProductoDTO dto) {
        Producto producto = productoMapper.toEntity(dto);
        return repository.save(producto).map(productoMapper::toDTO);
    }

    public Mono<ProductoDTO> actualizar(String id, ProductoDTO dto) {
        return repository.findById(id)
                .flatMap(productoExistente -> {
                    productoExistente.setNombre(dto.getNombre());
                    productoExistente.setPrecio(dto.getPrecio());
                    return repository.save(productoExistente);
                })
                .map(productoMapper::toDTO);
    }

    public Mono<ProductoDTO> eliminar(String id) {
        return repository.findById(id)
                .flatMap(producto -> repository.deleteById(producto.getId())
                        .thenReturn(productoMapper.toDTO(producto)));

    }

}
