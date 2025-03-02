package com.pruebatecnica.Inditex.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para crear o actualizar un precio")
public class PriceRequestDto {
    
    @Schema(description = "ID de la marca (1 = ZARA)", example = "1")
    private Long brandId;
    
    @Schema(description = "Fecha de inicio de aplicación", example = "2020-06-14T00:00:00")
    private LocalDateTime startDate;
    
    @Schema(description = "Fecha fin de aplicación", example = "2020-12-31T23:59:59")
    private LocalDateTime endDate;
    
    @Schema(description = "Identificador de la tarifa de precios", example = "1")
    private Long priceList;
    
    @Schema(description = "ID del producto", example = "35455")
    private Long productId;
    
    @Schema(description = "Prioridad para desambiguación (mayor valor = mayor prioridad)", example = "1")
    private Integer priority;
    
    @Schema(description = "Precio final del producto", example = "35.50")
    private BigDecimal price;
    
    @Schema(description = "Código ISO de la moneda", example = "EUR")
    private String currency;
}