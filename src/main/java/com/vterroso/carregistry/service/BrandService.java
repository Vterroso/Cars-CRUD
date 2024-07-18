package com.vterroso.carregistry.service;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.service.model.Brand;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> getAllBrands();
    Optional<Brand> getBrandById(Integer id);
    Brand addBrand(Brand brand);
    Optional<Brand> updateBrand(Integer id, Brand brand);
    void deleteBrand(Integer id);
}
