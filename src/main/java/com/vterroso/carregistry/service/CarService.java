package com.vterroso.carregistry.service;

import com.vterroso.carregistry.service.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CarService {
    public CompletableFuture<List<Car>> getAllCars();
    Optional<Car> getCarById(Integer id);
    Car addCar(Car car);
    Optional<Car> updateCar(Integer id, Car car);
    void deleteCar(Integer id);
}
