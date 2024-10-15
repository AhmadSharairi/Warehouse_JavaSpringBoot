package com.springjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSupplyDocumentDto 
{  
    
    private String  Name;
    private String  Subject;
    private String  createdBy;
    private String status;
    private Long   warehouseId; 
    private Long itemId;      

}
