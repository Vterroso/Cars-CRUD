package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.CarDTO;
import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.controller.mapper.CarMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;


    @GetMapping
    public CompletableFuture<ResponseEntity<List<CarWithBrandDTO>>> getAllCars() {
        return carService.getAllCars().thenApply(cars -> {
            List<CarWithBrandDTO> carWithBrandDTOs = cars.stream()
                    .map(carMapper::carToCarWithBrandDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(carWithBrandDTOs);
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarWithBrandDTO> getCarById(@PathVariable Integer id) {
        return carService.getCarById(id)
                .map(car -> ResponseEntity.ok(carMapper.carToCarWithBrandDTO(car)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarWithBrandDTO> createCar(@RequestBody CarDTO carDTO) {
        Car car = carMapper.INSTANCE.carDTOToCar(carDTO);
        Car savedCar = carService.addCar(car);
        CarWithBrandDTO savedCarWithBrandDTO = carMapper.carToCarWithBrandDTO(savedCar);
        return ResponseEntity.ok(savedCarWithBrandDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarWithBrandDTO> updateCar(@PathVariable Integer id, @RequestBody CarWithBrandDTO carWithBrandDTO) {
        Car car = carMapper.carWithBrandDTOToCar(carWithBrandDTO);
        return carService.updateCar(id, car)
                .map(updatedCar -> ResponseEntity.ok(carMapper.carToCarWithBrandDTO(updatedCar)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
