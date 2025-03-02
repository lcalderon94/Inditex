package com.pruebatecnica.Inditex.domain.port.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Puerto de entrada para el servicio de precios.
 * Define los casos de uso relacionados con la consulta de precios.
 */
public interface PriceService {
    
    /**
     * Obtiene el precio aplicable en base a los parámetros proporcionados.
     * 
     * @param date Fecha de aplicación
     * @param productId ID del producto
     * @param brandId ID de la marca
     * @return Objeto con la información del precio aplicable
     */
    PriceResponse findApplicablePrice(LocalDateTime date, Long productId, Long brandId);
    PriceResponse createPrice(PriceRequest priceRequest);
    PriceResponse getPrice(Long id);
    List<PriceResponse> getAllPrices();
    PriceResponse updatePrice(Long id, PriceRequest priceRequest);
    PriceResponse patchPrice(Long id, Map<String, Object> fields);
    void deletePrice(Long id);
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class PriceRequest {
        private Long brandId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long priceList;
        private Long productId;
        private Integer priority;
        private BigDecimal price;
        private String currency;
        
        // Getters, setters y constructor
    }
    
    /**
     * Clase inmutable para representar la respuesta del servicio.
     * Se define aquí para que el dominio no dependa de DTOs de la capa de aplicación.
     */
    class PriceResponse {
        private final Long productId;
        private final Long brandId;
        private final Long priceList;
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;
        private final java.math.BigDecimal price;
        
        public PriceResponse(Long productId, Long brandId, Long priceList, 
                            LocalDateTime startDate, LocalDateTime endDate, 
                            java.math.BigDecimal price) {
            this.productId = productId;
            this.brandId = brandId;
            this.priceList = priceList;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
        }
        
        public Long getProductId() {
            return productId;
        }
        
        public Long getBrandId() {
            return brandId;
        }
        
        public Long getPriceList() {
            return priceList;
        }
        
        public LocalDateTime getStartDate() {
            return startDate;
        }
        
        public LocalDateTime getEndDate() {
            return endDate;
        }
        
        public java.math.BigDecimal getPrice() {
            return price;
        }
    }
}
