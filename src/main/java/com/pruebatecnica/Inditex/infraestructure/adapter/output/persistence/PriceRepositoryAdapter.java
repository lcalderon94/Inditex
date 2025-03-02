package com.pruebatecnica.Inditex.infraestructure.adapter.output.persistence;


import org.springframework.stereotype.Component;

import com.pruebatecnica.Inditex.domain.model.Price;
import com.pruebatecnica.Inditex.domain.port.output.PriceRepository;
import com.pruebatecnica.Inditex.infraestructure.mapper.PriceMapper;
import com.pruebatecnica.Inditex.infraestructure.repository.JpaPriceRepository;
import com.pruebatecnica.Inditex.infraestructure.entity.PriceEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de precios.
 * Implementa el puerto de salida del dominio y utiliza la infraestructura JPA.
 */
@Component
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceMapper priceMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jpaPriceRepository Repositorio JPA
     * @param priceMapper Mapper para conversión de entidades
     */
    public PriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository, PriceMapper priceMapper) {
        this.jpaPriceRepository = jpaPriceRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<Price> findByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        List<PriceEntity> priceEntities = jpaPriceRepository.findByDateProductAndBrand(date, productId, brandId);
        return priceEntities.stream()
                .map(priceMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Price> findById(Long id) {
        return jpaPriceRepository.findById(id).map(priceMapper::toDomain);
    }

    @Override
    public List<Price> findAll() {
        return jpaPriceRepository.findAll().stream()
                .map(priceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Price save(Price price) {
        PriceEntity entity = priceMapper.toEntity(price);
        PriceEntity savedEntity = jpaPriceRepository.save(entity);
        return priceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaPriceRepository.deleteById(id);
    }
}
