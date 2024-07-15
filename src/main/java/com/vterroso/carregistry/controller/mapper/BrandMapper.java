package com.vterroso.carregistry.controller.mapper;

import com.vterroso.carregistry.service.model.Brand;
import com.vterroso.carregistry.controller.dto.BrandDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    @Mapping(target = "carEntityList", ignore = true)
    Brand brandDTOToBrand(BrandDTO brandDTO);
    BrandDTO brandToBrandDTO(Brand brand);

    List<BrandDTO> brandListToBrandDTOList(List<Brand> brandList);
    List<Brand> brandDTOListToBrandList(List<BrandDTO> brandDTOList);
}
