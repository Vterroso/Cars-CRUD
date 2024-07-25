package com.vterroso.carregistry.repository;


import com.vterroso.carregistry.repository.entity.CarEntity;
import com.vterroso.carregistry.service.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer>{
    @Query("SELECT c FROM CarEntity c LEFT JOIN FETCH c.brand")
    List<CarEntity> findAllWithBrand();

    @Query("SELECT c FROM CarEntity c LEFT JOIN FETCH c.brand WHERE c.id = :id")
    Optional<CarEntity> findByIdWithBrand(Integer id);
}
