package com.vterroso.carregistry.service;

import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.CarRepository;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.repository.entity.CarEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.repository.mapper.CarEntityMapper;
import com.vterroso.carregistry.service.impl.CarServiceImpl;
import com.vterroso.carregistry.service.model.Brand;
import com.vterroso.carregistry.service.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CarEntityMapper carEntityMapper;

    @Mock
    private BrandEntityMapper brandEntityMapper;

    private Car car;
    private CarEntity carEntity;
    private Brand brand;
    private BrandEntity brandEntity;

    @BeforeEach
    void setup() {
        brand = new Brand(1, "Toyota", 5, "Japan", null);
        car = new Car(1, brand, "Corolla", 15000, 20000.0, 2021, "A reliable family car", "Blue", "Gasoline", 4);
        brandEntity = new BrandEntity(1, "Toyota", 5, "Japan", null);
        carEntity = new CarEntity(1, brandEntity, "Corolla", 15000, 20000.0, 2021, "A reliable family car", "Blue", "Gasoline", 4);
    }

    @Test
    void getAllCars() throws Exception {
        when(carRepository.findAllWithBrand()).thenReturn(Collections.singletonList(carEntity));
        when(carEntityMapper.carEntityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> futureCars = carService.getAllCars();
        List<Car> cars = futureCars.get();

        assertEquals(1, cars.size());
        assertEquals(car, cars.get(0));
        verify(carRepository, times(1)).findAllWithBrand();
        verify(carEntityMapper, times(1)).carEntityToCar(carEntity);
    }

    @Test
    void getCarById() {
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(carEntityMapper.carEntityToCar(carEntity)).thenReturn(car);

        Optional<Car> result = carService.getCarById(1);

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
        verify(carRepository, times(1)).findById(1);
        verify(carEntityMapper, times(1)).carEntityToCar(carEntity);
    }

    @Test
    void addCar() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(brandEntityMapper.brandEntityToBrand(brandEntity)).thenReturn(brand);
        when(carEntityMapper.carToCarEntity(any(Car.class))).thenReturn(carEntity);
        when(carRepository.save(any(CarEntity.class))).thenReturn(carEntity);
        when(carEntityMapper.carEntityToCar(carEntity)).thenReturn(car);

        Car result = carService.addCar(car);

        assertEquals(car, result);
        verify(brandRepository, times(1)).findById(1);
        verify(brandEntityMapper, times(1)).brandEntityToBrand(brandEntity);
        verify(carEntityMapper, times(1)).carToCarEntity(any(Car.class));
        verify(carRepository, times(1)).save(any(CarEntity.class));
        verify(carEntityMapper, times(1)).carEntityToCar(carEntity);
    }

    @Test
    void updateCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(brandEntityMapper.brandToBrandEntity(any(Brand.class))).thenReturn(brandEntity);
        when(carRepository.save(any(CarEntity.class))).thenReturn(carEntity);
        when(carEntityMapper.carEntityToCar(carEntity)).thenReturn(car);

        Optional<Car> result = carService.updateCar(1, car);

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
        verify(carRepository, times(1)).findById(1);
        verify(brandEntityMapper, times(1)).brandToBrandEntity(any(Brand.class));
        verify(carRepository, times(1)).save(any(CarEntity.class));
        verify(carEntityMapper, times(1)).carEntityToCar(carEntity);
    }

    @Test
    void deleteCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));

        carService.deleteCar(1);

        verify(carRepository, times(1)).findById(1);
        verify(carRepository, times(1)).delete(carEntity);
    }

    @Test
    void deleteCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.deleteCar(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(carRepository, times(1)).findById(1);
        verify(carRepository, never()).delete(any(CarEntity.class));
    }
}
