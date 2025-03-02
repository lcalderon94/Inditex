package com.pruebatecnica.Inditex.application.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Ejemplo de campos actualizables en un precio")
public class PatchPrice {
    @Schema(description = "ID de la marca (1 = ZARA)", example = "1")
    private Long brandId;
    
    @Schema(description = "Fecha de inicio (opcional)", example = "2020-06-14T00:00:00")
    private String startDate;
    
    @Schema(description = "Fecha de fin (opcional)", example = "2020-12-31T23:59:59")
    private String endDate;
    
    @Schema(description = "ID de la tarifa de precios", example = "1")
    private Long priceList;
    
    @Schema(description = "ID del producto", example = "35455")
    private Long productId;
    
    @Schema(description = "Prioridad del precio", example = "1")
    private Integer priority;
    
    @Schema(description = "Precio final del producto", example = "42.99")
    private BigDecimal price;
    
    @Schema(description = "Moneda del precio", example = "EUR")
    private String currency;
}
