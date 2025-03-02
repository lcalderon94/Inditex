package com.pruebatecnica.Inditex.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pruebatecnica.Inditex.infraestructure.entity.PriceEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para acceder a la entidad de precios.
 */
@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    @Query("SELECT p FROM PriceEntity p WHERE p.productId = :productId " +
           "AND p.brandId = :brandId " +
           "AND :date BETWEEN p.startDate AND p.endDate")
    List<PriceEntity> findByDateProductAndBrand(
            @Param("date") LocalDateTime date, 
            @Param("productId") Long productId, 
            @Param("brandId") Long brandId);
}