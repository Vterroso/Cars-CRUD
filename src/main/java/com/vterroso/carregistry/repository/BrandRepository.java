package com.vterroso.carregistry.repository;

import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer>{
    @Query("SELECT b FROM BrandEntity b LEFT JOIN FETCH b.carEntityList WHERE b.id = :id")
    Optional<BrandEntity> findByIdWithCars(@Param("id") Integer id);

    @Query("SELECT b FROM BrandEntity b LEFT JOIN FETCH b.carEntityList")
    List<BrandEntity> findAllWithCars();
}
