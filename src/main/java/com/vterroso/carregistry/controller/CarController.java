package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.controller.mapper.CarMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarMapper carMapper;

    @GetMapping
    public ResponseEntity<List<CarWithBrandDTO>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        List<CarWithBrandDTO> carWithBrandDTOs = carMapper.carListToCarWithBrandDTOList(cars);
        return ResponseEntity.ok(carWithBrandDTOs);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarWithBrandDTO> getCarById(@PathVariable Integer id) {
        return carService.getCarById(id)
                .map(car -> ResponseEntity.ok(carMapper.carToCarWithBrandDTO(car)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<CarWithBrandDTO> createCar(@RequestBody CarWithBrandDTO carWithBrandDTO) {
        Car car = carMapper.carWithBrandDTOToCar(carWithBrandDTO);
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
        return ResponseEntity.ok().build();
    }
}