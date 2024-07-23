package com.vterroso.carregistry.service.impl;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.service.BrandService;
import com.vterroso.carregistry.service.model.Brand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandEntityMapper brandEntityMapper;


    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAllWithCars().stream()
                .map(brandEntityMapper::brandEntityToBrand)
                .toList();

    }

    @Override
    public Optional<Brand> getBrandById(Integer id) {
        return brandRepository.findByIdWithCars(id)
                .map(brandEntityMapper::brandEntityToBrand);
    }

    @Override
    public Brand addBrand(Brand brand) {
        BrandEntity brandEntity = brandEntityMapper.brandToBrandEntity(brand);
        BrandEntity savedBrandEntity = brandRepository.save(brandEntity);
        return brandEntityMapper.brandEntityToBrand(savedBrandEntity);
    }

    @Override
    public Optional<Brand> updateBrand(Integer id, Brand brand) {
        return brandRepository.findById(id).map(existingBrandEntity -> {
            existingBrandEntity.setName(brand.getName());
            existingBrandEntity.setWarranty(brand.getWarranty());
            existingBrandEntity.setCountry(brand.getCountry());

            BrandEntity updatedBrandEntity = brandRepository.save(existingBrandEntity);
            return brandEntityMapper.brandEntityToBrand(updatedBrandEntity);
        });
    }

    @Override
    public void deleteBrand(Integer id) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
        brandRepository.delete(brandEntity);
    }
}
