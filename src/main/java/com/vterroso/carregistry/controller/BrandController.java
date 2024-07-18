package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.controller.mapper.BrandMapper;
import com.vterroso.carregistry.service.BrandService;
import com.vterroso.carregistry.service.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> brandDTOs = brandMapper.brandListToBrandDTOList(brands);
        return ResponseEntity.ok(brandDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Integer id) {
        return brandService.getBrandById(id)
                .map(brand -> ResponseEntity.ok(brandMapper.brandToBrandDTO(brand)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);
        Brand savedBrand = brandService.addBrand(brand);
        BrandDTO savedBrandDTO = brandMapper.brandToBrandDTO(savedBrand);
        return ResponseEntity.ok(savedBrandDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Integer id, @RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);
        return brandService.updateBrand(id, brand)
                .map(updatedBrand -> ResponseEntity.ok(brandMapper.brandToBrandDTO(updatedBrand)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
