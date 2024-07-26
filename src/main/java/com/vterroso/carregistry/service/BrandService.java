package com.vterroso.carregistry.service;

import com.vterroso.carregistry.service.model.Brand;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BrandService {
    CompletableFuture<List<Brand>> getAllBrands();
    Optional<Brand> getBrandById(Integer id);
    Brand addBrand(Brand brand);
    Optional<Brand> updateBrand(Integer id, Brand brand);
    void deleteBrand(Integer id);
}
