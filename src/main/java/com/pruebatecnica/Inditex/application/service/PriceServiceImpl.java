package com.pruebatecnica.Inditex.application.service;

import com.pruebatecnica.Inditex.application.dto.PriceRequest;
import com.pruebatecnica.Inditex.application.dto.PriceResponse;
import com.pruebatecnica.Inditex.domain.exception.PriceNotFoundException;
import com.pruebatecnica.Inditex.domain.model.Price;
import com.pruebatecnica.Inditex.domain.port.input.PriceService;
import com.pruebatecnica.Inditex.domain.port.output.PriceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceResponse findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        List<Price> prices = priceRepository.findByDateProductAndBrand(date, productId, brandId);
        
        Price applicablePrice = prices.stream()
                .max(Comparator.comparing(Price::getPriority))
                .orElseThrow(() -> new PriceNotFoundException(
                        "No se encontró precio para fecha " + date + ", producto " + productId + ", marca " + brandId));
        
        return new PriceResponse(
                applicablePrice.getProductId(),
                applicablePrice.getBrandId(),
                applicablePrice.getPriceList(),
                applicablePrice.getStartDate(),
                applicablePrice.getEndDate(),
                applicablePrice.getPrice()
        );
    }
    
    @Override
    public PriceResponse createPrice(PriceRequest request) {
        Price price = Price.builder()
                .brandId(request.getBrandId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .priceList(request.getPriceList())
                .productId(request.getProductId())
                .priority(request.getPriority())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .build();
                
        Price savedPrice = priceRepository.save(price);
        
        return new PriceResponse(
                savedPrice.getProductId(),
                savedPrice.getBrandId(),
                savedPrice.getPriceList(),
                savedPrice.getStartDate(),
                savedPrice.getEndDate(),
                savedPrice.getPrice()
        );
    }
    
    @Override
    public PriceResponse getPrice(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException("Precio no encontrado con ID: " + id));
        
        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice()
        );
    }
    
    @Override
    public List<PriceResponse> getAllPrices() {
        return priceRepository.findAll().stream()
                .map(price -> new PriceResponse(
                        price.getProductId(),
                        price.getBrandId(),
                        price.getPriceList(),
                        price.getStartDate(),
                        price.getEndDate(),
                        price.getPrice()
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    public PriceResponse updatePrice(Long id, PriceRequest request) {
        if (!priceRepository.findById(id).isPresent()) {
            throw new PriceNotFoundException("Precio no encontrado con ID: " + id);
        }
        
        Price price = Price.builder()
                .id(id)
                .brandId(request.getBrandId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .priceList(request.getPriceList())
                .productId(request.getProductId())
                .priority(request.getPriority())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .build();
                
        Price savedPrice = priceRepository.save(price);
        
        return new PriceResponse(
                savedPrice.getProductId(),
                savedPrice.getBrandId(),
                savedPrice.getPriceList(),
                savedPrice.getStartDate(),
                savedPrice.getEndDate(),
                savedPrice.getPrice()
        );
    }
    
    @Override
    public PriceResponse patchPrice(Long id, Map<String, Object> fields) {
        Price existingPrice = priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException("Precio no encontrado con ID: " + id));
        
        fields.forEach((key, value) -> {
            if (value == null) return;
            
            switch (key) {
                case "brandId":
                    existingPrice.setBrandId(Long.valueOf(value.toString()));
                    break;
                case "startDate":
                    existingPrice.setStartDate(LocalDateTime.parse(value.toString()));
                    break;
                case "endDate":
                    existingPrice.setEndDate(LocalDateTime.parse(value.toString()));
                    break;
                case "priceList":
                    existingPrice.setPriceList(Long.valueOf(value.toString()));
                    break;
                case "productId":
                    existingPrice.setProductId(Long.valueOf(value.toString()));
                    break;
                case "priority":
                    existingPrice.setPriority(Integer.valueOf(value.toString()));
                    break;
                case "price":
                    existingPrice.setPrice(new BigDecimal(value.toString()));
                    break;
                case "currency":
                    existingPrice.setCurrency(value.toString());
                    break;
            }
        });
        
        Price savedPrice = priceRepository.save(existingPrice);
        
        return new PriceResponse(
                savedPrice.getProductId(),
                savedPrice.getBrandId(),
                savedPrice.getPriceList(),
                savedPrice.getStartDate(),
                savedPrice.getEndDate(),
                savedPrice.getPrice()
        );
    }
    
    @Override
    public void deletePrice(Long id) {
        if (!priceRepository.findById(id).isPresent()) {
            throw new PriceNotFoundException("Precio no encontrado con ID: " + id);
        }
        priceRepository.deleteById(id);
    }
}