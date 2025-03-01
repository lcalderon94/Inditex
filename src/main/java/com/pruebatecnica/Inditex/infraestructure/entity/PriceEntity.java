package com.pruebatecnica.Inditex.infraestructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla de precios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRICES")
public class PriceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;
    
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;
    
    @Column(name = "PRICE_LIST", nullable = false)
    private Long priceList;
    
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;
    
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    
    @Column(name = "CURR", nullable = false)
    private String currency;
}
