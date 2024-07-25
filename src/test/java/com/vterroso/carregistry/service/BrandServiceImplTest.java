package com.vterroso.carregistry.service;

import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.service.impl.BrandServiceImpl;
import com.vterroso.carregistry.service.model.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandEntityMapper brandEntityMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandEntity brandEntity;

    @BeforeEach
    void setUp() {
        brand = new Brand(1, "Toyota", 5, "Japan", null);
        brandEntity = new BrandEntity(1, "Toyota", 5, "Japan", null);
    }

    @Test
    void getAllBrands() throws Exception {
        List<BrandEntity> brandEntities = List.of(brandEntity);
        List<Brand> brands = List.of(brand);

        when(brandRepository.findAll()).thenReturn(brandEntities);
        when(brandEntityMapper.brandEntityToBrand(brandEntity)).thenReturn(brand);

        CompletableFuture<List<Brand>> result = brandService.getAllBrands();
        assertEquals(brands, result.get());

        verify(brandRepository, times(1)).findAll();
        verify(brandEntityMapper, times(1)).brandEntityToBrand(brandEntity);
    }

    @Test
    void getBrandById() {
        when(brandRepository.findByIdWithCars(1)).thenReturn(Optional.of(brandEntity));
        when(brandEntityMapper.brandEntityToBrand(brandEntity)).thenReturn(brand);

        Optional<Brand> result = brandService.getBrandById(1);
        assertTrue(result.isPresent());
        assertEquals(brand, result.get());

        verify(brandRepository, times(1)).findByIdWithCars(1);
        verify(brandEntityMapper, times(1)).brandEntityToBrand(brandEntity);
    }

    @Test
    void addBrand() {
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandEntityMapper.brandToBrandEntity(brand)).thenReturn(brandEntity);
        when(brandEntityMapper.brandEntityToBrand(brandEntity)).thenReturn(brand);

        Brand result = brandService.addBrand(brand);
        assertEquals(brand, result);

        verify(brandRepository, times(1)).save(brandEntity);
        verify(brandEntityMapper, times(1)).brandToBrandEntity(brand);
        verify(brandEntityMapper, times(1)).brandEntityToBrand(brandEntity);
    }

    @Test
    void updateBrand() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandEntityMapper.brandEntityToBrand(brandEntity)).thenReturn(brand);

        Optional<Brand> result = brandService.updateBrand(1, brand);
        assertTrue(result.isPresent());
        assertEquals(brand, result.get());

        verify(brandRepository, times(1)).findById(1);
        verify(brandRepository, times(1)).save(brandEntity);
        verify(brandEntityMapper, times(1)).brandEntityToBrand(brandEntity);
    }

    @Test
    void deleteBrand() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));

        brandService.deleteBrand(1);

        verify(brandRepository, times(1)).findById(1);
        verify(brandRepository, times(1)).delete(brandEntity);
    }

    @Test
    void deleteBrand_NotFound() {
        when(brandRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> brandService.deleteBrand(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(brandRepository, times(1)).findById(1);
        verify(brandRepository, never()).delete(any());
    }
}
