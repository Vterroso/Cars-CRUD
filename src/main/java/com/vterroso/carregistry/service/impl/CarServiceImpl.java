package com.vterroso.carregistry.service.impl;

import com.vterroso.carregistry.repository.BrandRepository;
import com.vterroso.carregistry.repository.CarRepository;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.repository.entity.CarEntity;
import com.vterroso.carregistry.repository.mapper.BrandEntityMapper;
import com.vterroso.carregistry.repository.mapper.CarEntityMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Brand;
import com.vterroso.carregistry.service.model.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;



@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarEntityMapper carEntityMapper;
    private final BrandEntityMapper brandEntityMapper;
    private final String[] HEADERS = {"id", "brand", "model", "mileage", "price",
            "year", "description", "colour", "fuelType", "numDoors"};


    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Car>> getAllCars() {
        long start = System.currentTimeMillis();
        List<Car> cars = carRepository.findAllWithBrand().stream()
                .map(carEntityMapper::carEntityToCar)
                .toList();
        long end = System.currentTimeMillis();
        log.info("Time elapsed: {} ms", end - start);
        return CompletableFuture.completedFuture(cars);
    }


    @Override
    public Optional<Car> getCarById(Integer id) {
        return carRepository.findById(id)
                .map(carEntityMapper::carEntityToCar);
    }

    @Override
    public Car addCar(Car car) {
        // Verify that the brand is not null and has an id
        if (car.getBrand() == null || car.getBrand().getId() == null) {
            throw new IllegalArgumentException("Brand must be specified.");
        }
        log.info("Adding car: {}", car);
        Brand brand = brandRepository.findById(car.getBrand().getId())
                .map(brandEntityMapper::brandEntityToBrand)
                .orElseThrow(() -> new IllegalArgumentException("Brand does not exist."));

        car.setBrand(brand);

        CarEntity carEntity = carEntityMapper.carToCarEntity(car);
        carEntity.setBrand(brandEntityMapper.brandToBrandEntity(car.getBrand()));
        CarEntity savedCarEntity = carRepository.save(carEntity);

        log.info("Car added: {}", savedCarEntity);
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
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        log.info("Deleting car: {}", carEntity);
        carRepository.delete(carEntity);
    }

    public String getCarsCsv(){
        List<CarEntity> carEntityList = carRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(Arrays.toString(HEADERS)).append("\n");

        for(CarEntity car : carEntityList){
            csvContent
                    .append(car.getId()).append(", ")
                    .append(car.getBrand().getName()).append(", ")
                    .append(car.getModel()).append(", ")
                    .append(car.getMileage()).append(", ")
                    .append(car.getPrice()).append(", ")
                    .append(car.getYear()).append(", ")
                    .append(car.getDescription()).append(", ")
                    .append(car.getColour()).append(", ")
                    .append(car.getFuelType()).append(", ")
                    .append(car.getNumDoors()).append("\n");
        }
        return csvContent.toString();
    }

    @Override
    public List<Car> uploadCars(MultipartFile file) {
        List<CarEntity> carEntityList = new ArrayList<>();
        List<Car> carList;


        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))){

            // Parse csv
            CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase().withTrim());

            // Get each csv line
            Iterable<CSVRecord> records = parser.getRecords();

            // Add attributes from csv to cars
            for (CSVRecord recordLine : records) {
                CarEntity car = new CarEntity();
                String brandName = recordLine.get("brand");

                // Check brand exist and set it
                BrandEntity brand = brandRepository
                        .findByName(brandName)
                        .orElseThrow(()-> new NoSuchElementException("Brand not found"));

                car.setBrand(brand);
                car.setModel(recordLine.get("model"));
                car.setMileage(Integer.valueOf(recordLine.get("mileage")));
                car.setPrice(Double.valueOf( recordLine.get("price")));
                car.setYear(Integer.valueOf(recordLine.get("year")));
                car.setDescription(recordLine.get("description"));
                car.setColour(recordLine.get("colour"));
                car.setFuelType(recordLine.get("fuelType"));
                car.setNumDoors(Integer.valueOf(recordLine.get("numDoors")));

                carEntityList.add(car);
            }

            // Save entities
            carList = carEntityMapper.carEntityListToCarList(carRepository.saveAll(carEntityList));
            log.info("All users saved");


        } catch (IOException e) {
            log.error("Failed to load cars");
            throw new RuntimeException("Failed to load cars: " + e.getMessage(), e);
        }catch (NoSuchElementException e) {
            log.error("Brand not found", e);
            throw new RuntimeException("Failed to load cars: " + e.getMessage(), e);
        }
        return carList;
    }
}
