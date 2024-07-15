package com.vterroso.carregistry.controller.mapper;

import com.vterroso.carregistry.controller.dto.CarDTO;
import com.vterroso.carregistry.controller.dto.CarWithBrandDTO;
import com.vterroso.carregistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = BrandMapper.class)
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "brand.name", source = "brandName")
    Car carDTOToCar(CarDTO carDTO);

    @Mapping(target = "brandName", source = "brand.name")
    CarDTO carToCarDTO(Car car);

    // Car with brand object
    @Mapping(target = "brand", source = "brand")
    CarWithBrandDTO carToCarWithBrandDTO(Car car);

    @Mapping(target = "brand", source = "brand")
    Car carWithBrandDTOToCar(CarWithBrandDTO carWithBrandDTO);

    @Mapping(target = "brand", ignore = true)
    List<CarWithBrandDTO> carListToCarWithBrandDTOList(List<Car> carList);

    @Mapping(target = "brand", ignore = true)
    List<Car> carWithBrandDTOListToCarList(List<CarWithBrandDTO> carWithBrandDTOList);

    // Car without brand object
    List<Car> carDTOListToCarList(List<CarDTO> carDTOList);

    List<CarDTO> carListToCarDTOList(List<Car> carList);
}