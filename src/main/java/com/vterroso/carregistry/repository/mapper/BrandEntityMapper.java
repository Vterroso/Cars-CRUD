package com.vterroso.carregistry.repository.mapper;

import com.vterroso.carregistry.repository.entity.BrandEntity;
import com.vterroso.carregistry.service.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandEntityMapper {
    @Mapping(target = "carEntityList", ignore = true)
    Brand brandEntityToBrand(BrandEntity brandEntity);
    @Mapping(target = "carEntityList", ignore = true)
    BrandEntity brandToBrandEntity(Brand brand);

    List<Brand> brandEntityToBrandList(List<BrandEntity> brandEntityList);
    List<BrandEntity> brandListToBrandEntityList(List<Brand> brandList);
}
