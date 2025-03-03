package com.pruebatecnica.Inditex.infrastucture.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatecnica.Inditex.application.dto.PriceRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PriceControllerIntegrationTest {
	
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    // Tests obligatorios de la prueba técnica
    @Test
    void test1_shouldReturnPriceAt10amOnDay14() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(1)))
                .andExpect(jsonPath("$.price", is(35.50)));
    }
    
    @Test
    void test2_shouldReturnPriceAt4pmOnDay14() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(2)))
                .andExpect(jsonPath("$.price", is(25.45)));
    }
    
    @Test
    void test3_shouldReturnPriceAt9pmOnDay14() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(1)))
                .andExpect(jsonPath("$.price", is(35.50)));
    }
    
    @Test
    void test4_shouldReturnPriceAt10amOnDay15() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-15T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(3)))
                .andExpect(jsonPath("$.price", is(30.50)));
    }
    
    @Test
    void test5_shouldReturnPriceAt9pmOnDay16() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-16T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(4)))
                .andExpect(jsonPath("$.price", is(38.95)));
    }
    
    // Test adicionales para validación de parámetros
    @Test
    void shouldReturnBadRequestWithInvalidFormatDate() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14") // Formato de fecha incompleto
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldReturnBadRequestWithInvalidProductId() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T10:00:00")
                .param("productId", "invalid")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }
    
    // Test para validar comportamiento con fechas límite
    @Test
    void shouldReturnPriceAtExactStartDate() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T15:00:00") // Exactamente el inicio de price2
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(2)))
                .andExpect(jsonPath("$.price", is(25.45)));
    }
    
    @Test
    void shouldReturnPriceAtExactEndDate() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T18:30:00") // Exactamente el fin de price2
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(2)))
                .andExpect(jsonPath("$.price", is(25.45)));
    }
    
    // Test para validar el flujo completo de CRUD
    @Test
    void shouldPerformFullCrudLifecycle() throws Exception {
        // 1. Crear un nuevo precio
        PriceRequestDto newPriceDto = PriceRequestDto.builder()
                .brandId(1L)
                .startDate(LocalDateTime.parse("2023-01-01T00:00:00"))
                .endDate(LocalDateTime.parse("2023-12-31T23:59:59"))
                .priceList(10L)
                .productId(35455L)
                .priority(0)
                .price(new BigDecimal("45.99"))
                .currency("EUR")
                .build();
        
        String newPriceJson = objectMapper.writeValueAsString(newPriceDto);
        
        // Crear precio y obtener la respuesta
        MvcResult createResult = mockMvc.perform(post("/api/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPriceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price", is(45.99)))
                .andExpect(jsonPath("$.priceList", is(10)))
                .andDo(print())
                .andReturn();
                
        // Ya que no tenemos getId() en PriceResponseDto, usaremos el priceList para identificar
        // Este enfoque funciona siempre que el priceList sea único
        
        // 2. Obtener el precio por priceList (en un entorno real, sería mejor por ID)
        mockMvc.perform(get("/api/prices")
                .param("priceList", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priceList", is(10)))
                .andExpect(jsonPath("$[0].price", is(45.99)));
                
        // Alternativa: Buscar todos y filtrar
        String responseJson = createResult.getResponse().getContentAsString();
        // Aquí podríamos analizar el JSON para extraer algún identificador
        
        // 3. Actualizar parcialmente el precio (asumimos que sabemos que el ID es 5 para el nuevo precio)
        // Nota: En un escenario real, deberíamos obtener el ID del nuevo recurso
        mockMvc.perform(patch("/api/prices/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\": 50.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(50.99)))
                .andExpect(jsonPath("$.priceList", is(10)));
        
        // 4. Eliminar el precio
        mockMvc.perform(delete("/api/prices/5"))
                .andExpect(status().isNoContent());
        
        // 5. Verificar que ya no existe
        mockMvc.perform(get("/api/prices/5"))
                .andExpect(status().isNotFound());
    }
    
    // Test para validar el comportamiento con consultas en fechas específicas
    @Test
    void shouldReturnCorrectPriceForDateRangeWithMultipleOverlaps() throws Exception {
        // Configurar fecha que coincide con tres rangos diferentes
        LocalDateTime testDate = LocalDateTime.parse("2020-06-14T16:00:00");
        
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", testDate.toString())
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList", is(2))) // Tarifa con mayor prioridad
                .andExpect(jsonPath("$.price", is(25.45)));
    }
    
    // Test para validar comportamiento con productos inexistentes
    @Test
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T10:00:00")
                .param("productId", "99999") // Producto que no existe
                .param("brandId", "1"))
                .andExpect(status().isNotFound());
    }
    
    // Test para validar comportamiento con marcas inexistentes
    @Test
    void shouldReturnNotFoundForNonExistentBrand() throws Exception {
        mockMvc.perform(get("/api/prices/applicable")
                .param("date", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "99")) // Marca que no existe
                .andExpect(status().isNotFound());
    }
    
    // Pruebas adicionales que ya existían
    @Test
    void shouldCreateNewPrice() throws Exception {
        String requestBody = "{\"brandId\":1,\"startDate\":\"2023-01-01T00:00:00\",\"endDate\":\"2023-12-31T23:59:59\",\"priceList\":5,\"productId\":35455,\"priority\":1,\"price\":42.99,\"currency\":\"EUR\"}";
        
        mockMvc.perform(post("/api/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.price", is(42.99)));
    }
    
    @Test
    void shouldGetPriceById() throws Exception {
        mockMvc.perform(get("/api/prices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)));
    }
    
    @Test
    void shouldUpdatePrice() throws Exception {
        String requestBody = "{\"brandId\":1,\"startDate\":\"2020-06-14T00:00:00\",\"endDate\":\"2020-12-31T23:59:59\",\"priceList\":1,\"productId\":35455,\"priority\":0,\"price\":39.95,\"currency\":\"EUR\"}";
        
        mockMvc.perform(put("/api/prices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(39.95)));
    }
    
    @Test
    void shouldPatchPrice() throws Exception {
        String requestBody = "{\"price\":29.99}";
        
        mockMvc.perform(patch("/api/prices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(29.99)));
    }

    @Test
    void shouldDeletePrice() throws Exception {
        mockMvc.perform(delete("/api/prices/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenPriceDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/prices/999"))
                .andExpect(status().isNotFound());
    }
}