package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.controller.mapper.CarMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'VENDOR')")
    public ResponseEntity<List<CarWithBrandDTO>> getAllCars() {
        try {
            List<Car> cars = carService.getAllCars().get();
            List<CarWithBrandDTO> carWithBrandDTOs = cars.stream()
                    .map(carMapper::carToCarWithBrandDTO)
                    .toList();
            return ResponseEntity.ok(carWithBrandDTOs);
        } catch (Exception e) {
            log.error("Error fetching cars", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'VENDOR')")
    public ResponseEntity<CarWithBrandDTO> getCarById(@PathVariable Integer id) {
        return carService.getCarById(id)
                .map(car -> ResponseEntity.ok(carMapper.carToCarWithBrandDTO(car)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CarWithBrandDTO> createCar(@RequestBody CarWithBrandDTO carWithBrandDTO) {
        try {
            Car car = carMapper.carWithBrandDTOToCar(carWithBrandDTO);
            Car savedCar = carService.addCar(car);
            CarWithBrandDTO savedCarWithBrandDTO = carMapper.carToCarWithBrandDTO(savedCar);
            return ResponseEntity.ok(savedCarWithBrandDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CarWithBrandDTO> updateCar(@PathVariable Integer id, @RequestBody CarWithBrandDTO carWithBrandDTO) {
        Car car = carMapper.carWithBrandDTOToCar(carWithBrandDTO);
        return carService.updateCar(id, car)
                .map(updatedCar -> ResponseEntity.ok(carMapper.carToCarWithBrandDTO(updatedCar)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
