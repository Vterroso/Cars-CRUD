package com.vterroso.carregistry.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Brand {
    private Integer id;
    private String name;
    private Integer warranty;
    private String country;
    private List<Car> carEntityList;
}
