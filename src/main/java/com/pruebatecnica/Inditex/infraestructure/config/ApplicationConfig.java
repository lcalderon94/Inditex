package com.pruebatecnica.Inditex.infraestructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pruebatecnica.Inditex.application.service.PriceServiceImpl;
import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.domain.port.output.PriceRepository;

/**
 * Configuración de la aplicación.
 * Registra los beans necesarios para conectar las capas de la aplicación.
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Registra el servicio de precios como un bean de Spring.
     * 
     * @param priceRepository Repositorio de precios inyectado por Spring
     * @return Instancia del servicio de precios
     */
    @Bean
    public PriceService priceService(PriceRepository priceRepository) {
        return new PriceServiceImpl(priceRepository);
    }
}