package com.vterroso.carregistry.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {
    Integer id;
    private String name;
    private Integer warranty;
    private String country;
    private List<CarDTO> carEntityList;
}
