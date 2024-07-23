package com.vterroso.carregistry.controller.mapper;

import com.vterroso.carregistry.service.model.Brand;
import com.vterroso.carregistry.controller.dto.BrandDTO;
import com.vterroso.carregistry.repository.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "carEntityList", ignore = true)
    Brand brandDTOToBrand(BrandDTO brandDTO);

    BrandDTO brandToBrandDTO(Brand brand);

    List<BrandDTO> brandListToBrandDTOList(List<Brand> brandList);

    List<Brand> brandDTOListToBrandList(List<BrandDTO> brandDTOList);

    Brand brandEntityToBrand(BrandEntity brandEntity);

    BrandEntity brandToBrandEntity(Brand brand);

    List<Brand> brandEntityListToBrandList(List<BrandEntity> brandEntities);

    List<BrandEntity> brandListToBrandEntityList(List<Brand> brands);
}
