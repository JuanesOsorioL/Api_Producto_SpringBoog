package com.example.Producto.mapper;

import com.example.Producto.dto.ProductoDTO;
import com.example.Producto.entity.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoDTO toDTO(Producto producto);
    Producto toEntity(ProductoDTO productoDTO);
}
