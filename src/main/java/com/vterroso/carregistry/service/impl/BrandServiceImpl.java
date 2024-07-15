package com.vterroso.carregistry.service.impl;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.controller.mapper.BrandMapper;
import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.service.BrandService;
import com.vterroso.carregistry.service.model.Brand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandEntityMapper brandEntityMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandEntityMapper::brandEntityToBrand)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Brand> getBrandById(Integer id) {
        return brandRepository.findById(id)
                .map(brandEntityMapper::brandEntityToBrand);
    }

    @Override
    public Brand addBrand(Brand brand) {
        BrandEntity brandEntity = brandEntityMapper.brandToBrandEntity(brand);
        BrandEntity savedBrandEntity = brandRepository.save(brandEntity);
        return brandEntityMapper.brandEntityToBrand(savedBrandEntity);
    }
    @Override
    public ResponseEntity<BrandDTO> updateBrand(Integer id, Brand brand) {
        return brandRepository.findById(id).map(existingBrandEntity -> {
            existingBrandEntity.setName(brand.getName());
            existingBrandEntity.setWarranty(brand.getWarranty());
            existingBrandEntity.setCountry(brand.getCountry());
            BrandEntity updatedBrandEntity = brandRepository.save(existingBrandEntity);
            Brand updatedBrand = brandEntityMapper.brandEntityToBrand(updatedBrandEntity);
            BrandDTO updatedBrandDTO = brandMapper.brandToBrandDTO(updatedBrand);
            return ResponseEntity.ok(updatedBrandDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Override
    public Optional<BrandEntity> deleteBrand(Integer id) {
        Optional<BrandEntity> brandOptional = brandRepository.findById(id);
        brandOptional.ifPresent(brand -> brandRepository.delete(brand));
        return brandOptional;
    }
}
