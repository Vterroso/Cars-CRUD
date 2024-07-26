package com.vterroso.carregistry.controller;

import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.controller.mapper.CarMapper;
import com.vterroso.carregistry.service.CarService;
import com.vterroso.carregistry.service.model.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @GetMapping(value = "/downloadCars")
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<?> downloadCars()throws IOException {
        // Set the content type and attachment header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachement","cars.csv");

        // Download cars
        byte[] csvBytes = carService.getCarsCsv().getBytes();
        log.info("Cars downloaded");
        return new ResponseEntity<>(csvBytes,headers, HttpStatus.OK);
    }

    @PostMapping(value = "/uploadCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> uploadCsv(@RequestParam(value = "file")MultipartFile file) {

        if (file.isEmpty()){
            log.error("The file is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

        if(Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")){
            log.info("File name: {}", file.getOriginalFilename());

            // Add users from csv
            List<CarWithBrandDTO> carsList = carMapper.carListToCarWithBrandDTOList(carService.uploadCars(file));
            log.info("File successfully updated.");
            return ResponseEntity.ok(carsList);
        }

        log.error("The file is not a csv file");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }




}
