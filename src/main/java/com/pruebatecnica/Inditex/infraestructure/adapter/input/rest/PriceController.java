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

import com.pruebatecnica.Inditex.application.dto.PriceRequest;
import com.pruebatecnica.Inditex.application.dto.PriceRequestDto;
import com.pruebatecnica.Inditex.application.dto.PriceResponse;
import com.pruebatecnica.Inditex.application.dto.PriceResponseDto;
import com.pruebatecnica.Inditex.application.dto.PatchPrice;

import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.infraestructure.mapper.PriceMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el endpoint de precios.
 */
@RestController
@RequestMapping("/api/prices")
@Tag(name = "Precios", description = "API para la gestión de precios según criterios temporales y de negocio")
public class PriceController {
    private final PriceService priceService;
    private final PriceMapper priceMapper;
    
    public PriceController(PriceService priceService, PriceMapper priceMapper) {
        this.priceService = priceService;
        this.priceMapper = priceMapper;
    }
    
    @GetMapping("/applicable")
    @Operation(
        summary = "Obtiene el precio aplicable según fecha, producto y marca",
        description = "Devuelve el precio a aplicar considerando la fecha, el producto y la marca. En caso de tarifas coincidentes, selecciona la de mayor prioridad."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Precio encontrado", 
                    content = @Content(schema = @Schema(implementation = PriceResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Precio no encontrado"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<PriceResponseDto> getApplicablePrice(
            @Parameter(description = "Fecha de aplicación (ISO Date Time)", example = "2020-06-14T10:00:00", required = true)
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            
            @Parameter(description = "ID del producto", example = "35455", required = true)
            @RequestParam(name = "productId") Long productId,
            
            @Parameter(description = "ID de la marca (1 = ZARA)", example = "1", required = true)
            @RequestParam(name = "brandId") Long brandId) {
        
        PriceResponse priceResponse = priceService.findApplicablePrice(date, productId, brandId);
        PriceResponseDto responseDto = priceMapper.toDto(priceResponse);
        
        return ResponseEntity.ok(responseDto);
    }
    
    @PostMapping
    @Operation(
        summary = "Crea un nuevo precio",
        description = "Registra un nuevo precio con toda la información necesaria"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Precio creado correctamente", 
                    content = @Content(schema = @Schema(implementation = PriceResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PriceResponseDto> createPrice(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del precio a crear", 
                required = true, 
                content = @Content(schema = @Schema(implementation = PriceRequestDto.class))
            )
            @RequestBody PriceRequestDto requestDto) {
        PriceRequest request = priceMapper.toDomainRequest(requestDto);
        PriceResponse response = priceService.createPrice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(priceMapper.toDto(response));
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtiene un precio por su ID",
        description = "Recupera toda la información de un precio específico mediante su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Precio encontrado", 
                    content = @Content(schema = @Schema(implementation = PriceResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Precio no encontrado")
    })
    public ResponseEntity<PriceResponseDto> getPrice(
            @Parameter(description = "ID del precio", example = "1", required = true)
            @PathVariable(name = "id") Long id) {
        PriceResponse response = priceService.getPrice(id);
        return ResponseEntity.ok(priceMapper.toDto(response));
    }
    
    @GetMapping
    @Operation(
        summary = "Obtiene todos los precios registrados",
        description = "Devuelve un listado completo de todos los precios en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de precios recuperada correctamente",
        content = @Content(schema = @Schema(implementation = PriceResponseDto.class))
    )
    public ResponseEntity<List<PriceResponseDto>> getAllPrices() {
        List<PriceResponseDto> prices = priceService.getAllPrices().stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prices);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualiza un precio existente",
        description = "Actualiza completamente un precio identificado por su ID con nueva información"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Precio actualizado correctamente", 
                    content = @Content(schema = @Schema(implementation = PriceResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Precio no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PriceResponseDto> updatePrice(
            @Parameter(description = "ID del precio a actualizar", example = "1", required = true)
            @PathVariable(name = "id") Long id, 
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos actualizados del precio", 
                required = true,
                content = @Content(schema = @Schema(implementation = PriceRequestDto.class))
            )
            @RequestBody PriceRequestDto requestDto) {
        
        PriceRequest request = priceMapper.toDomainRequest(requestDto);
        PriceResponse response = priceService.updatePrice(id, request);
        return ResponseEntity.ok(priceMapper.toDto(response));
    }
    
    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualiza parcialmente un precio",
        description = "Permite actualizar solo algunos campos específicos de un precio existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Precio actualizado parcialmente", 
                    content = @Content(schema = @Schema(implementation = PriceResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Precio no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PriceResponseDto> patchPrice(
            @Parameter(description = "ID del precio a actualizar parcialmente", example = "1", required = true)
            @PathVariable(name = "id") Long id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Campos a actualizar (puede incluir cualquiera de los campos del precio)", 
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PatchPrice.class)
                )
            )
            @RequestBody Map<String, Object> fields) {
        
        PriceResponse response = priceService.patchPrice(id, fields);
        return ResponseEntity.ok(priceMapper.toDto(response));
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Elimina un precio",
        description = "Elimina un precio existente mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Precio eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Precio no encontrado")
    })
    public ResponseEntity<Void> deletePrice(
            @Parameter(description = "ID del precio a eliminar", example = "1", required = true)
            @PathVariable(name = "id") Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}