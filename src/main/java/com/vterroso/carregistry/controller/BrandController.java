package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.controller.mapper.BrandMapper;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.service.impl.BrandServiceImpl;
import com.vterroso.carregistry.service.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandServiceImpl brandServiceImpl;

    @Autowired
    private BrandMapper brandMapper;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        try {
            return ResponseEntity.ok(brandMapper.brandListToBrandDTOList(brandServiceImpl.getAllBrands()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Integer id) {
        return brandServiceImpl.getBrandById(id)
                .map(brand -> ResponseEntity.ok(brandMapper.brandToBrandDTO(brand)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        return ResponseEntity.ok(brandMapper.brandToBrandDTO(brandServiceImpl.addBrand(brandMapper.brandDTOToBrand(brandDTO))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Integer id, @RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);

        return brandServiceImpl.updateBrand(id, brand);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        Optional<BrandEntity> brandOptional = brandServiceImpl.deleteBrand(id);
        return brandOptional.isPresent() ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}