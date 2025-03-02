package com.pruebatecnica.Inditex.infraestructure.adapter.input.rest;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.Inditex.application.dto.PriceRequestDto;
import com.pruebatecnica.Inditex.application.dto.PriceResponseDto;

import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.infraestructure.mapper.PriceMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el endpoint de precios.
 */
//src/main/java/com/example/pricesapi/infrastructure/adapter/input/rest/PriceController.java
@RestController
@RequestMapping("/api/prices")
public class PriceController {
 private final PriceService priceService;
 private final PriceMapper priceMapper;
 
 public PriceController(PriceService priceService, PriceMapper priceMapper) {
     this.priceService = priceService;
     this.priceMapper = priceMapper;
 }
 
 // Método existente para consulta específica
 @GetMapping("/applicable")
 public ResponseEntity<PriceResponseDto> getApplicablePrice(
         @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
         @RequestParam(name = "productId") Long productId,
         @RequestParam(name = "brandId") Long brandId) {
     
     PriceService.PriceResponse priceResponse = priceService.findApplicablePrice(date, productId, brandId);
     PriceResponseDto responseDto = priceMapper.toDto(priceResponse);
     
     return ResponseEntity.ok(responseDto);
 }
 
 // Crear nuevo precio
 @PostMapping
 public ResponseEntity<PriceResponseDto> createPrice(@RequestBody PriceRequestDto requestDto) {
     PriceService.PriceRequest request = priceMapper.toDomainRequest(requestDto);
     PriceService.PriceResponse response = priceService.createPrice(request);
     return ResponseEntity.status(HttpStatus.CREATED).body(priceMapper.toDto(response));
 }
 
 // Obtener precio por ID
 @GetMapping("/{id}")
 public ResponseEntity<PriceResponseDto> getPrice(@PathVariable(name = "id") Long id) {
     PriceService.PriceResponse response = priceService.getPrice(id);
     return ResponseEntity.ok(priceMapper.toDto(response));
 }
 
 // Listar todos los precios
 @GetMapping
 public ResponseEntity<List<PriceResponseDto>> getAllPrices() {
     List<PriceResponseDto> prices = priceService.getAllPrices().stream()
             .map(priceMapper::toDto)
             .collect(Collectors.toList());
     return ResponseEntity.ok(prices);
 }
 
 // Actualizar precio completamente
 @PutMapping("/{id}")
 public ResponseEntity<PriceResponseDto> updatePrice(
         @PathVariable(name = "id") Long id, 
         @RequestBody PriceRequestDto requestDto) {
     
     PriceService.PriceRequest request = priceMapper.toDomainRequest(requestDto);
     PriceService.PriceResponse response = priceService.updatePrice(id, request);
     return ResponseEntity.ok(priceMapper.toDto(response));
 }
 
 // Actualizar precio parcialmente (PATCH)
 @PatchMapping("/{id}")
 public ResponseEntity<PriceResponseDto> patchPrice(
         @PathVariable(name = "id") Long id,
         @RequestBody Map<String, Object> fields) {
     
     PriceService.PriceResponse response = priceService.patchPrice(id, fields);
     return ResponseEntity.ok(priceMapper.toDto(response));
 }
 
 // Eliminar precio
 @DeleteMapping("/{id}")
 public ResponseEntity<Void> deletePrice(@PathVariable(name = "id") Long id) {
     priceService.deletePrice(id);
     return ResponseEntity.noContent().build();
 }
}
