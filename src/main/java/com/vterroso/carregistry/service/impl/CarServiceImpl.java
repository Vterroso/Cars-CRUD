package com.vterroso.carregistry.service.impl;

import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.CarRepository;
import com.vterroso.carregistry.repository.entity.CarEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.repository.mapper.CarEntityMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CarEntityMapper carEntityMapper;

    @Autowired
    private BrandEntityMapper brandEntityMapper;


    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll().stream()
                .map(carEntityMapper::carEntityToCar)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Car> getCarById(Integer id) {
        return carRepository.findById(id)
                .map(carEntityMapper::carEntityToCar);

    }

    @Override
    public Car addCar(Car car) {
        CarEntity carEntity = carEntityMapper.carToCarEntity(car);
        carEntity.setBrand(brandEntityMapper.brandToBrandEntity(car.getBrand()));
        CarEntity savedCarEntity = carRepository.save(carEntity);
        return carEntityMapper.carEntityToCar(savedCarEntity);
    }

    @Override
    public Optional<Car> updateCar(Integer id, Car car) {
        return carRepository.findById(id).map(existingCarEntity -> {
            existingCarEntity.setModel(car.getModel());
            existingCarEntity.setMileage(car.getMileage());
            existingCarEntity.setPrice(car.getPrice());
            existingCarEntity.setYear(car.getYear());
            existingCarEntity.setDescription(car.getDescription());
            existingCarEntity.setColour(car.getColour());
            existingCarEntity.setFuelType(car.getFuelType());
            existingCarEntity.setNumDoors(car.getNumDoors());

            if (car.getBrand() != null) {
                existingCarEntity.setBrand(brandEntityMapper.brandToBrandEntity(car.getBrand()));
            }

            CarEntity updatedCarEntity = carRepository.save(existingCarEntity);
            return carEntityMapper.carEntityToCar(updatedCarEntity);
        });
    }
    @Override
    public void deleteCar(Integer id) {
        carRepository.findById(id).ifPresent(carRepository::delete);
    }
}
