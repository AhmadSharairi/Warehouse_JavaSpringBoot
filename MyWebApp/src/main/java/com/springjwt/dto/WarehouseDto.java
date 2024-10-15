package com.springjwt.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {

    // @JsonIgnore
    private Long id; 

    private String warehouseName;

    private String warehouseDescription;
    
    private String createdBy;

    private List<ItemDto> items;
}
