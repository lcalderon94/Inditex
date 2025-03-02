package com.pruebatecnica.Inditex.domain.port.output;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.pruebatecnica.Inditex.domain.model.Price;

/**
 * Puerto de salida para el repositorio de precios.
 * Define operaciones para acceder a datos de precios.
 */
public interface PriceRepository {
	List<Price> findByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId);
    Optional<Price> findById(Long id);
    List<Price> findAll();
    Price save(Price price);
    void deleteById(Long id);
}