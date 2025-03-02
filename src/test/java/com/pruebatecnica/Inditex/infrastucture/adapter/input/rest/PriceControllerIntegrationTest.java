package com.pruebatecnica.Inditex.infrastucture.adapter.input.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PriceControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;

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
    
    //Cosas que no iban en la prueba tecnica pero igual queria probar a ver si funcionaban correctamente
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
