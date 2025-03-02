package com.pruebatecnica.Inditex.application.service;

import com.pruebatecnica.Inditex.domain.exception.PriceNotFoundException;
import com.pruebatecnica.Inditex.domain.model.Price;
import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.domain.port.input.PriceService.PriceRequest;
import com.pruebatecnica.Inditex.domain.port.input.PriceService.PriceResponse;
import com.pruebatecnica.Inditex.domain.port.output.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceServiceImpl priceService;

    private Price price1;
    private Price price2;
    private Price price3;
    private Price price4;
    private PriceRequest priceRequest;

    @BeforeEach
    void setUp() {
        // Configurar los precios de prueba según los datos del ejemplo
        price1 = Price.builder()
                .id(1L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priceList(1L)
                .productId(35455L)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();
        
        price2 = Price.builder()
                .id(2L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
                .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
                .priceList(2L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();
        
        price3 = Price.builder()
                .id(3L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
                .priceList(3L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("30.50"))
                .currency("EUR")
                .build();
        
        price4 = Price.builder()
                .id(4L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priceList(4L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("38.95"))
                .currency("EUR")
                .build();
                
        // Creación de un PriceRequest para pruebas de creación/actualización
        priceRequest = PriceService.PriceRequest.builder()
                .brandId(1L)
                .startDate(LocalDateTime.parse("2023-01-01T00:00:00"))
                .endDate(LocalDateTime.parse("2023-12-31T23:59:59"))
                .priceList(5L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("42.99"))
                .currency("EUR")
                .build();
    }

    @Test
    void shouldFindApplicablePriceForTest1() {
        // Test 1: 10:00 del día 14
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1));

        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        assertEquals(1L, result.getPriceList());
        assertEquals(new BigDecimal("35.50"), result.getPrice());
    }
    
    @Test
    void shouldFindApplicablePriceForTest2() {
        // Test 2: 16:00 del día 14
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1, price2));

        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
    }
    
    @Test
    void shouldFindApplicablePriceForTest3() {
        // Test 3: 21:00 del día 14
        LocalDateTime date = LocalDateTime.parse("2020-06-14T21:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1));

        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        assertEquals(1L, result.getPriceList());
        assertEquals(new BigDecimal("35.50"), result.getPrice());
    }
    
    @Test
    void shouldFindApplicablePriceForTest4() {
        // Test 4: 10:00 del día 15
        LocalDateTime date = LocalDateTime.parse("2020-06-15T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1, price3));

        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        assertEquals(3L, result.getPriceList());
        assertEquals(new BigDecimal("30.50"), result.getPrice());
    }
    
    @Test
    void shouldFindApplicablePriceForTest5() {
        // Test 5: 21:00 del día 16
        LocalDateTime date = LocalDateTime.parse("2020-06-16T21:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1, price4));

        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        assertEquals(4L, result.getPriceList());
        assertEquals(new BigDecimal("38.95"), result.getPrice());
    }
    
    @Test
    void shouldCreatePrice() {
        // Crear un nuevo Price basado en PriceRequest
        Price newPrice = Price.builder()
                .brandId(priceRequest.getBrandId())
                .startDate(priceRequest.getStartDate())
                .endDate(priceRequest.getEndDate())
                .priceList(priceRequest.getPriceList())
                .productId(priceRequest.getProductId())
                .priority(priceRequest.getPriority())
                .price(priceRequest.getPrice())
                .currency(priceRequest.getCurrency())
                .build();
                
        // Configurar el mock
        Price savedPrice = Price.builder()
                .id(5L)
                .brandId(priceRequest.getBrandId())
                .startDate(priceRequest.getStartDate())
                .endDate(priceRequest.getEndDate())
                .priceList(priceRequest.getPriceList())
                .productId(priceRequest.getProductId())
                .priority(priceRequest.getPriority())
                .price(priceRequest.getPrice())
                .currency(priceRequest.getCurrency())
                .build();
                
        when(priceRepository.save(any(Price.class))).thenReturn(savedPrice);
        
        // Ejecutar el servicio
        PriceResponse result = priceService.createPrice(priceRequest);
        
        // Verificar
        assertNotNull(result);
        assertEquals(priceRequest.getBrandId(), result.getBrandId());
        assertEquals(priceRequest.getProductId(), result.getProductId());
        assertEquals(priceRequest.getPriceList(), result.getPriceList());
        assertEquals(priceRequest.getPrice(), result.getPrice());
        verify(priceRepository).save(any(Price.class));
    }
    
    @Test
    void shouldGetPriceById() {
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        
        PriceResponse result = priceService.getPrice(1L);
        
        assertNotNull(result);
        assertEquals(price1.getProductId(), result.getProductId());
        assertEquals(price1.getBrandId(), result.getBrandId());
        assertEquals(price1.getPriceList(), result.getPriceList());
        assertEquals(price1.getPrice(), result.getPrice());
    }
    
    @Test
    void shouldGetAllPrices() {
        when(priceRepository.findAll()).thenReturn(List.of(price1, price2, price3, price4));
        
        List<PriceResponse> results = priceService.getAllPrices();
        
        assertEquals(4, results.size());
        assertEquals(price1.getProductId(), results.get(0).getProductId());
        assertEquals(price2.getProductId(), results.get(1).getProductId());
        assertEquals(price3.getProductId(), results.get(2).getProductId());
        assertEquals(price4.getProductId(), results.get(3).getProductId());
    }
    
    @Test
    void shouldUpdatePrice() {
        // Crear el precio actualizado
        Price updatedPrice = Price.builder()
                .id(1L)
                .brandId(priceRequest.getBrandId())
                .startDate(priceRequest.getStartDate())
                .endDate(priceRequest.getEndDate())
                .priceList(priceRequest.getPriceList())
                .productId(priceRequest.getProductId())
                .priority(priceRequest.getPriority())
                .price(priceRequest.getPrice())
                .currency(priceRequest.getCurrency())
                .build();
                
        // Configurar mocks
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        when(priceRepository.save(any(Price.class))).thenReturn(updatedPrice);
        
        // Ejecutar servicio
        PriceResponse result = priceService.updatePrice(1L, priceRequest);
        
        // Verificar
        assertNotNull(result);
        assertEquals(priceRequest.getPriceList(), result.getPriceList());
        assertEquals(priceRequest.getPrice(), result.getPrice());
        verify(priceRepository).save(any(Price.class));
    }
    
    @Test
    void shouldPatchPrice() {
        // Configurar datos
        Map<String, Object> fields = new HashMap<>();
        fields.put("price", "50.00");
        
        Price patchedPrice = Price.builder()
                .id(1L)
                .brandId(price1.getBrandId())
                .startDate(price1.getStartDate())
                .endDate(price1.getEndDate())
                .priceList(price1.getPriceList())
                .productId(price1.getProductId())
                .priority(price1.getPriority())
                .price(new BigDecimal("50.00"))
                .currency(price1.getCurrency())
                .build();
                
        // Configurar mocks
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        when(priceRepository.save(any(Price.class))).thenReturn(patchedPrice);
        
        // Ejecutar servicio
        PriceResponse result = priceService.patchPrice(1L, fields);
        
        // Verificar
        assertNotNull(result);
        assertEquals(new BigDecimal("50.00"), result.getPrice());
        assertEquals(price1.getPriceList(), result.getPriceList());
        verify(priceRepository).save(any(Price.class));
    }
    
    @Test
    void shouldDeletePrice() {
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        
        priceService.deletePrice(1L);
        
        verify(priceRepository).deleteById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenPriceNotFound() {
        // Test para verificar que se lanza excepción cuando no se encuentra un precio
        LocalDateTime date = LocalDateTime.parse("2019-01-01T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 99999L, 1L))
                .thenReturn(Collections.emptyList());
                
        assertThrows(PriceNotFoundException.class, () -> 
            priceService.findApplicablePrice(date, 99999L, 1L)
        );
    }
    
    @Test
    void shouldThrowExceptionWhenPriceNotFoundById() {
        when(priceRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(PriceNotFoundException.class, () -> 
            priceService.getPrice(999L)
        );
    }
    
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPrice() {
        when(priceRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(PriceNotFoundException.class, () -> 
            priceService.updatePrice(999L, priceRequest)
        );
    }
    
    @Test
    void shouldThrowExceptionWhenPatchingNonExistentPrice() {
        when(priceRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(PriceNotFoundException.class, () -> 
            priceService.patchPrice(999L, Collections.singletonMap("price", "50.00"))
        );
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPrice() {
        when(priceRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(PriceNotFoundException.class, () -> 
            priceService.deletePrice(999L)
        );
    }
}