package com.pruebatecnica.Inditex.infrastucture.adapter.input.rest;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.pruebatecnica.Inditex.application.dto.PriceResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void applicationShouldStartAndServeRequests() {
        // Realizar una solicitud y verificar que la aplicación responde correctamente
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(
                "/api/prices/applicable?date=2020-06-14T10:00:00&productId=35455&brandId=1",
                PriceResponseDto.class);
                
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getPriceList());
        assertEquals(new BigDecimal("35.50"), response.getBody().getPrice());
    }
}
