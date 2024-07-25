package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.controller.mapper.CarMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCars() {
        Car car = new Car();
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();

        when(carService.getAllCars()).thenReturn(CompletableFuture.completedFuture(List.of(car)));
        when(carMapper.carToCarWithBrandDTO(car)).thenReturn(carWithBrandDTO);

        ResponseEntity<List<CarWithBrandDTO>> response = carController.getAllCars();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(carService, times(1)).getAllCars();
        verify(carMapper, times(1)).carToCarWithBrandDTO(car);
    }

    @Test
    public void testGetCarById() {
        Car car = new Car();
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();

        when(carService.getCarById(anyInt())).thenReturn(Optional.of(car));
        when(carMapper.carToCarWithBrandDTO(car)).thenReturn(carWithBrandDTO);

        ResponseEntity<CarWithBrandDTO> response = carController.getCarById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carWithBrandDTO, response.getBody());
        verify(carService, times(1)).getCarById(1);
        verify(carMapper, times(1)).carToCarWithBrandDTO(car);
    }

    @Test
    public void testGetCarById_NotFound() {
        when(carService.getCarById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<CarWithBrandDTO> response = carController.getCarById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(carService, times(1)).getCarById(1);
        verify(carMapper, times(0)).carToCarWithBrandDTO(any(Car.class));
    }

    @Test
    public void testCreateCar() {
        Car car = new Car();
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();

        when(carMapper.carWithBrandDTOToCar(any(CarWithBrandDTO.class))).thenReturn(car);
        when(carService.addCar(any(Car.class))).thenReturn(car);
        when(carMapper.carToCarWithBrandDTO(any(Car.class))).thenReturn(carWithBrandDTO);

        ResponseEntity<CarWithBrandDTO> response = carController.createCar(carWithBrandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carWithBrandDTO, response.getBody());
        verify(carMapper, times(1)).carWithBrandDTOToCar(carWithBrandDTO);
        verify(carService, times(1)).addCar(car);
        verify(carMapper, times(1)).carToCarWithBrandDTO(car);
    }

    @Test
    void testCreateCar_BadRequest() {
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();
        Car car = new Car();

        when(carMapper.carWithBrandDTOToCar(carWithBrandDTO)).thenReturn(car);

        doThrow(new IllegalArgumentException("Brand must be specified.")).when(carService).addCar(any(Car.class));

        ResponseEntity<CarWithBrandDTO> response = carController.createCar(carWithBrandDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(carMapper, times(1)).carWithBrandDTOToCar(carWithBrandDTO);
        verify(carService, times(1)).addCar(any(Car.class));
    }

    @Test
    public void testUpdateCar() {
        Car car = new Car();
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();

        when(carMapper.carWithBrandDTOToCar(any(CarWithBrandDTO.class))).thenReturn(car);
        when(carService.updateCar(anyInt(), any(Car.class))).thenReturn(Optional.of(car));
        when(carMapper.carToCarWithBrandDTO(any(Car.class))).thenReturn(carWithBrandDTO);

        ResponseEntity<CarWithBrandDTO> response = carController.updateCar(1, carWithBrandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carWithBrandDTO, response.getBody());
        verify(carMapper, times(1)).carWithBrandDTOToCar(carWithBrandDTO);
        verify(carService, times(1)).updateCar(1, car);
        verify(carMapper, times(1)).carToCarWithBrandDTO(car);
    }

    @Test
    public void testUpdateCar_NotFound() {
        Car car = new Car();
        CarWithBrandDTO carWithBrandDTO = new CarWithBrandDTO();

        when(carMapper.carWithBrandDTOToCar(any(CarWithBrandDTO.class))).thenReturn(car);
        when(carService.updateCar(anyInt(), any(Car.class))).thenReturn(Optional.empty());

        ResponseEntity<CarWithBrandDTO> response = carController.updateCar(1, carWithBrandDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(carMapper, times(1)).carWithBrandDTOToCar(carWithBrandDTO);
        verify(carService, times(1)).updateCar(1, car);
        verify(carMapper, times(0)).carToCarWithBrandDTO(any(Car.class));
    }

    @Test
    public void testDeleteCar() {
        doNothing().when(carService).deleteCar(anyInt());

        ResponseEntity<Void> response = carController.deleteCar(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService, times(1)).deleteCar(1);
    }
}
