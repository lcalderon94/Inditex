package com.pruebatecnica.Inditex.application.service;

import com.pruebatecnica.Inditex.application.dto.PriceRequest;
import com.pruebatecnica.Inditex.application.dto.PriceResponse;
import com.pruebatecnica.Inditex.domain.exception.PriceNotFoundException;
import com.pruebatecnica.Inditex.domain.model.Price;
import com.pruebatecnica.Inditex.domain.port.input.PriceService;

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
        priceRequest = PriceRequest.builder()
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
        // Configurar el mock con el precio que debería devolver al guardar
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
    
    //Test para probar los limites de fechas
    @Test
    void shouldFindPriceExactlyAtStartDate() {
        // Probar justo en el límite de inicio de la tarifa
        LocalDateTime exactStartDate = LocalDateTime.parse("2020-06-14T15:00:00");
        when(priceRepository.findByDateProductAndBrand(exactStartDate, 35455L, 1L))
                .thenReturn(List.of(price1, price2));
        
        PriceResponse result = priceService.findApplicablePrice(exactStartDate, 35455L, 1L);
        
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
    }

    @Test
    void shouldFindPriceExactlyAtEndDate() {
        // Probar justo en el límite de fin de la tarifa
        LocalDateTime exactEndDate = LocalDateTime.parse("2020-06-14T18:30:00");
        when(priceRepository.findByDateProductAndBrand(exactEndDate, 35455L, 1L))
                .thenReturn(List.of(price1, price2));
        
        PriceResponse result = priceService.findApplicablePrice(exactEndDate, 35455L, 1L);
        
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
    }

    @Test
    void shouldFindPriceOneSecondAfterStartDate() {
        // Probar un segundo después del inicio
        LocalDateTime oneSecondAfterStart = LocalDateTime.parse("2020-06-14T15:00:01");
        when(priceRepository.findByDateProductAndBrand(oneSecondAfterStart, 35455L, 1L))
                .thenReturn(List.of(price1, price2));
        
        PriceResponse result = priceService.findApplicablePrice(oneSecondAfterStart, 35455L, 1L);
        
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
    }

    @Test
    void shouldFindPriceOneSecondBeforeEndDate() {
        // Probar un segundo antes del fin
        LocalDateTime oneSecondBeforeEnd = LocalDateTime.parse("2020-06-14T18:29:59");
        when(priceRepository.findByDateProductAndBrand(oneSecondBeforeEnd, 35455L, 1L))
                .thenReturn(List.of(price1, price2));
        
        PriceResponse result = priceService.findApplicablePrice(oneSecondBeforeEnd, 35455L, 1L);
        
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
    }
    
    //Test de prioridad y seleccion
    @Test
    void shouldSelectHighestPriorityWhenMultiplePricesApply() {
        // Crear un precio con prioridad más alta
        Price highPriorityPrice = Price.builder()
                .id(6L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T23:59:59"))
                .priceList(5L)
                .productId(35455L)
                .priority(2) // Mayor prioridad que los demás
                .price(new BigDecimal("40.50"))
                .currency("EUR")
                .build();
                
        LocalDateTime date = LocalDateTime.parse("2020-06-15T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1, price3, highPriorityPrice));
                
        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        // Debe seleccionar el precio con la prioridad más alta
        assertEquals(5L, result.getPriceList());
        assertEquals(new BigDecimal("40.50"), result.getPrice());
    }

    @Test
    void shouldHandleMultiplePricesWithSamePriority() {
        // Crear un precio con la misma prioridad pero fecha más reciente
        Price samePriorityNewerPrice = Price.builder()
                .id(7L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-15T09:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
                .priceList(6L)
                .productId(35455L)
                .priority(1) // Misma prioridad que price3
                .price(new BigDecimal("33.50"))
                .currency("EUR")
                .build();
                
        LocalDateTime date = LocalDateTime.parse("2020-06-15T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(List.of(price1, price3, samePriorityNewerPrice));
                
        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 1L);
        
        // Con la misma prioridad, verificamos que sea uno de los dos precios con prioridad 1
        assertTrue(
            result.getPriceList().equals(3L) || result.getPriceList().equals(6L),
            "Debe seleccionar uno de los precios con prioridad 1"
        );
        
        // Verificar que el precio seleccionado coincida con uno de los esperados
        assertTrue(
            result.getPrice().equals(new BigDecimal("30.50")) || 
            result.getPrice().equals(new BigDecimal("33.50")),
            "El precio debe coincidir con uno de los items de prioridad 1"
        );
    }
    
    //Test adicionales en los escenarios de negocio
    @Test
    void shouldReturnEmptyResponseWhenNoPricesExist() {
        // Escenario sin precios
        LocalDateTime date = LocalDateTime.parse("2021-01-01T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 1L))
                .thenReturn(Collections.emptyList());
                
        // Debe lanzar una excepción cuando no hay precios aplicables
        PriceNotFoundException exception = assertThrows(
            PriceNotFoundException.class,
            () -> priceService.findApplicablePrice(date, 35455L, 1L)
        );
        
        assertTrue(exception.getMessage().contains("No se encontró precio"));
    }

    @Test
    void shouldHandleDifferentBrandsCorrectly() {
        // Prueba con marca diferente
        Price otherBrandPrice = Price.builder()
                .id(8L)
                .brandId(2L) // Otra marca
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priceList(7L)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("45.50"))
                .currency("EUR")
                .build();
                
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35455L, 2L))
                .thenReturn(List.of(otherBrandPrice));
                
        PriceResponse result = priceService.findApplicablePrice(date, 35455L, 2L);
        
        assertEquals(2L, result.getBrandId());
        assertEquals(7L, result.getPriceList());
        assertEquals(new BigDecimal("45.50"), result.getPrice());
    }

    @Test
    void shouldHandleDifferentProductsCorrectly() {
        // Prueba con producto diferente
        Price otherProductPrice = Price.builder()
                .id(9L)
                .brandId(1L)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priceList(8L)
                .productId(35456L) // Otro producto
                .priority(1)
                .price(new BigDecimal("99.50"))
                .currency("EUR")
                .build();
                
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        when(priceRepository.findByDateProductAndBrand(date, 35456L, 1L))
                .thenReturn(List.of(otherProductPrice));
                
        PriceResponse result = priceService.findApplicablePrice(date, 35456L, 1L);
        
        assertEquals(35456L, result.getProductId());
        assertEquals(8L, result.getPriceList());
        assertEquals(new BigDecimal("99.50"), result.getPrice());
    }
    
    //Pruebas de validacion especificas
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullFields() {
        // Prueba con campos nulos en la actualización
        PriceRequest nullRequest = null;
        
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        
        assertThrows(NullPointerException.class, () -> 
            priceService.updatePrice(1L, nullRequest)
        );
    }

    @Test
    void shouldHandlePatchWithInvalidFieldValues() {
        // Prueba con valores inválidos en patch
        Map<String, Object> invalidFields = new HashMap<>();
        invalidFields.put("price", "invalid-price");
        
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        
        assertThrows(NumberFormatException.class, () -> 
            priceService.patchPrice(1L, invalidFields)
        );
    }

    @Test
    void shouldHandlePatchWithUnknownFields() {
        // Prueba con campos desconocidos
        Map<String, Object> unknownFields = new HashMap<>();
        unknownFields.put("unknownField", "value");
        unknownFields.put("price", "45.50");
        
        Price patchedPrice = Price.builder()
                .id(1L)
                .brandId(price1.getBrandId())
                .startDate(price1.getStartDate())
                .endDate(price1.getEndDate())
                .priceList(price1.getPriceList())
                .productId(price1.getProductId())
                .priority(price1.getPriority())
                .price(new BigDecimal("45.50"))
                .currency(price1.getCurrency())
                .build();
                
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price1));
        when(priceRepository.save(any(Price.class))).thenReturn(patchedPrice);
        
        // El campo desconocido debe ser ignorado pero no causar error
        PriceResponse result = priceService.patchPrice(1L, unknownFields);
        
        assertEquals(new BigDecimal("45.50"), result.getPrice());
        // El resto de campos deben permanecer igual
        assertEquals(price1.getBrandId(), result.getBrandId());
        assertEquals(price1.getProductId(), result.getProductId());
    }
}