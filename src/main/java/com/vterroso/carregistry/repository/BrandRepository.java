package com.vterroso.carregistry.repository;

import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer>{
}
