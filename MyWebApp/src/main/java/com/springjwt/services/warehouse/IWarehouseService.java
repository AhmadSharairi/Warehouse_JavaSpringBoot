package com.springjwt.services.warehouse;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Warehouse;


public interface IWarehouseService {

    List<Warehouse> getAllWarehouses();
    WarehouseDto getWarehouseById(Long id);
    void addWarehouse(Warehouse warehouse);
    void updateWarehouse(Warehouse warehouse);
    boolean warehouseExists(Long id);
    Warehouse createWarehouse(WarehouseDto warehouseDto); 
    Optional<Warehouse> updateWarehouse(Long id, WarehouseDto warehouseDto);
    List<String> getItemsByWarehouseId(Long warehouseId);
    ResponseEntity<Warehouse> deleteWarehouse(Long warehouseId);
    Warehouse createWarehouseWithItems(WarehouseDto warehouseDto);
    List<WarehouseInfoDto> getAllWarehouseInfo();

}
 