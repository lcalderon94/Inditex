package com.pruebatecnica.Inditex.domain.port.input;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.pruebatecnica.Inditex.application.dto.PriceRequest;
import com.pruebatecnica.Inditex.application.dto.PriceResponse;



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
    
    
    
}
