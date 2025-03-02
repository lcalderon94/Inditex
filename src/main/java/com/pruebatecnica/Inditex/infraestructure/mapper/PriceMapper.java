package com.pruebatecnica.Inditex.infraestructure.mapper;


import org.springframework.stereotype.Component;

import com.pruebatecnica.Inditex.application.dto.PriceRequestDto;
import com.pruebatecnica.Inditex.application.dto.PriceResponseDto;
import com.pruebatecnica.Inditex.domain.model.Price;
import com.pruebatecnica.Inditex.infraestructure.entity.PriceEntity;
import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.domain.port.input.PriceService.PriceResponse;

/**
 * Mapea entre diferentes representaciones de precios.
 */
@Component
public class PriceMapper {
    
    /**
     * Convierte una entidad JPA a un modelo de dominio.
     * 
     * @param entity Entidad JPA
     * @return Modelo de dominio
     */
    public Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Price.builder()
                .id(entity.getId())
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .build();
    }
    
    /**
     * Convierte un modelo de dominio a una entidad JPA.
     * 
     * @param domain Modelo de dominio
     * @return Entidad JPA
     */
    public PriceEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }
        
        return PriceEntity.builder()
                .id(domain.getId())
                .brandId(domain.getBrandId())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .priceList(domain.getPriceList())
                .productId(domain.getProductId())
                .priority(domain.getPriority())
                .price(domain.getPrice())
                .currency(domain.getCurrency())
                .build();
    }
    
    /**
     * Convierte una respuesta del servicio de dominio a un DTO.
     * 
     * @param response Respuesta del servicio de dominio
     * @return DTO para la respuesta API
     */
    public PriceResponseDto toDto(PriceResponse response) {
        if (response == null) {
            return null;
        }
        
        return PriceResponseDto.builder()
                .productId(response.getProductId())
                .brandId(response.getBrandId())
                .priceList(response.getPriceList())
                .startDate(response.getStartDate())
                .endDate(response.getEndDate())
                .price(response.getPrice())
                .build();
    }
    
    /**
     * Convierte un DTO de petición a un objeto de petición del servicio.
     *
     * @param dto DTO de petición
     * @return Objeto de petición para el servicio
     */
    public PriceService.PriceRequest toDomainRequest(PriceRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return PriceService.PriceRequest.builder()
                .brandId(dto.getBrandId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .priceList(dto.getPriceList())
                .productId(dto.getProductId())
                .priority(dto.getPriority())
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .build();
    }
}
