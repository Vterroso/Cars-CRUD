package com.vterroso.carregistry.repository.mapper;

import com.vterroso.carregistry.repository.entity.CarEntity;
import com.vterroso.carregistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarEntityMapper {

        CarEntityMapper mapper = Mappers.getMapper(CarEntityMapper.class);

        CarEntity carToCarEntity(Car car);
        Car carEntityToCar(CarEntity carEntity);
        List<CarEntity> carListToCarEntityList(List<Car> carList);
        List<Car> carEntityListToCarList(List<CarEntity> carEntityList);
}
