package com.pruebatecnica.Inditex.infrastucture.adapter.input.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.pruebatecnica.Inditex.infraestructure.entity.PriceEntity;
import com.pruebatecnica.Inditex.infraestructure.repository.JpaPriceRepository;

@SpringBootTest
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PriceRepositoryIntegrationTests {

    @Autowired
    private JpaPriceRepository jpaPriceRepository;
    
    @Test
    void shouldFindPricesByDateProductAndBrand() {
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        List<PriceEntity> prices = jpaPriceRepository.findByDateProductAndBrand(date, 35455L, 1L);
        
        assertNotNull(prices);
        assertEquals(2, prices.size());
        
        // Verificar que los precios corresponden a los esperados
        boolean containsPrice1 = prices.stream()
                .anyMatch(p -> p.getPriceList() == 1L && p.getPrice().compareTo(new BigDecimal("35.50")) == 0);
                
        boolean containsPrice2 = prices.stream()
                .anyMatch(p -> p.getPriceList() == 2L && p.getPrice().compareTo(new BigDecimal("25.45")) == 0);
                
        assertTrue(containsPrice1 && containsPrice2);
    }
    
    @Test
    void shouldPerformCrudOperationsOnDatabase() {
        // 1. Insertar nuevo precio
        PriceEntity newPrice = PriceEntity.builder()
                .brandId(1L)
                .startDate(LocalDateTime.parse("2023-01-01T00:00:00"))
                .endDate(LocalDateTime.parse("2023-12-31T23:59:59"))
                .priceList(5L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("42.99"))
                .currency("EUR")
                .build();
                
        PriceEntity savedPrice = jpaPriceRepository.save(newPrice);
        assertNotNull(savedPrice.getId());
        
        // 2. Leer el precio guardado
        Optional<PriceEntity> retrievedPrice = jpaPriceRepository.findById(savedPrice.getId());
        assertTrue(retrievedPrice.isPresent());
        assertEquals(new BigDecimal("42.99"), retrievedPrice.get().getPrice());
        
        // 3. Actualizar el precio
        retrievedPrice.get().setPrice(new BigDecimal("45.99"));
        PriceEntity updatedPrice = jpaPriceRepository.save(retrievedPrice.get());
        assertEquals(new BigDecimal("45.99"), updatedPrice.getPrice());
        
        // 4. Eliminar el precio
        jpaPriceRepository.deleteById(updatedPrice.getId());
        assertFalse(jpaPriceRepository.findById(updatedPrice.getId()).isPresent());
    }
    
    @Test
    void shouldHandleDateRanges() {
        // Test con fechas exactamente en los límites
        LocalDateTime exactStartDate = LocalDateTime.parse("2020-06-14T15:00:00");
        List<PriceEntity> pricesAtStart = jpaPriceRepository.findByDateProductAndBrand(exactStartDate, 35455L, 1L);
        assertEquals(2, pricesAtStart.size());
        
        LocalDateTime exactEndDate = LocalDateTime.parse("2020-06-14T18:30:00");
        List<PriceEntity> pricesAtEnd = jpaPriceRepository.findByDateProductAndBrand(exactEndDate, 35455L, 1L);
        assertEquals(2, pricesAtEnd.size());
        
        // Test con fecha fuera de todos los rangos
        LocalDateTime outOfRangeDate = LocalDateTime.parse("2019-01-01T00:00:00");
        List<PriceEntity> outOfRangePrices = jpaPriceRepository.findByDateProductAndBrand(outOfRangeDate, 35455L, 1L);
        assertTrue(outOfRangePrices.isEmpty());
    }
}