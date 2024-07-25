package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.controller.mapper.BrandMapper;
import com.vterroso.carregistry.service.BrandService;
import com.vterroso.carregistry.service.model.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBrands() throws Exception {
        Brand brand = new Brand();
        BrandDTO brandDTO = new BrandDTO();

        when(brandService.getAllBrands()).thenReturn(CompletableFuture.completedFuture(List.of(brand)));
        when(brandMapper.brandToBrandDTO(brand)).thenReturn(brandDTO);

        ResponseEntity<List<BrandDTO>> response = brandController.getAllBrands().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(brandService, times(1)).getAllBrands();
        verify(brandMapper, times(1)).brandToBrandDTO(brand);
    }

    @Test
    public void testGetBrandById() {
        Brand brand = new Brand();
        BrandDTO brandDTO = new BrandDTO();

        when(brandService.getBrandById(1)).thenReturn(Optional.of(brand));
        when(brandMapper.brandToBrandDTO(brand)).thenReturn(brandDTO);

        ResponseEntity<BrandDTO> response = brandController.getBrandById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(brandDTO, response.getBody());
        verify(brandService, times(1)).getBrandById(1);
        verify(brandMapper, times(1)).brandToBrandDTO(brand);
    }

    @Test
    public void testGetBrandByIdNotFound() {
        when(brandService.getBrandById(1)).thenReturn(Optional.empty());

        ResponseEntity<BrandDTO> response = brandController.getBrandById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(brandService, times(1)).getBrandById(1);
        verify(brandMapper, never()).brandToBrandDTO(any());
    }

    @Test
    public void testCreateBrand() {
        Brand brand = new Brand();
        BrandDTO brandDTO = new BrandDTO();
        Brand savedBrand = new Brand();
        BrandDTO savedBrandDTO = new BrandDTO();

        when(brandMapper.brandDTOToBrand(brandDTO)).thenReturn(brand);
        when(brandService.addBrand(brand)).thenReturn(savedBrand);
        when(brandMapper.brandToBrandDTO(savedBrand)).thenReturn(savedBrandDTO);

        ResponseEntity<BrandDTO> response = brandController.createBrand(brandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedBrandDTO, response.getBody());
        verify(brandMapper, times(1)).brandDTOToBrand(brandDTO);
        verify(brandService, times(1)).addBrand(brand);
        verify(brandMapper, times(1)).brandToBrandDTO(savedBrand);
    }

    @Test
    public void testUpdateBrand() {
        Brand brand = new Brand();
        BrandDTO brandDTO = new BrandDTO();
        Brand updatedBrand = new Brand();
        BrandDTO updatedBrandDTO = new BrandDTO();

        when(brandMapper.brandDTOToBrand(brandDTO)).thenReturn(brand);
        when(brandService.updateBrand(1, brand)).thenReturn(Optional.of(updatedBrand));
        when(brandMapper.brandToBrandDTO(updatedBrand)).thenReturn(updatedBrandDTO);

        ResponseEntity<BrandDTO> response = brandController.updateBrand(1, brandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBrandDTO, response.getBody());
        verify(brandMapper, times(1)).brandDTOToBrand(brandDTO);
        verify(brandService, times(1)).updateBrand(1, brand);
        verify(brandMapper, times(1)).brandToBrandDTO(updatedBrand);
    }

    @Test
    public void testUpdateBrandNotFound() {
        Brand brand = new Brand();
        BrandDTO brandDTO = new BrandDTO();

        when(brandMapper.brandDTOToBrand(brandDTO)).thenReturn(brand);
        when(brandService.updateBrand(1, brand)).thenReturn(Optional.empty());

        ResponseEntity<BrandDTO> response = brandController.updateBrand(1, brandDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(brandMapper, times(1)).brandDTOToBrand(brandDTO);
        verify(brandService, times(1)).updateBrand(1, brand);
        verify(brandMapper, never()).brandToBrandDTO(any());
    }

    @Test
    public void testDeleteBrand() {
        doNothing().when(brandService).deleteBrand(1);

        ResponseEntity<Void> response = brandController.deleteBrand(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(brandService, times(1)).deleteBrand(1);
    }
}
