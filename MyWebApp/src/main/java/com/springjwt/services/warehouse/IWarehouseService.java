package com.springjwt.services.warehouse;

import java.util.List;
import java.util.Optional;
import com.springjwt.dto.ItemDto;
import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Warehouse;
import jakarta.servlet.http.HttpServletResponse;

public interface IWarehouseService {

    List<Warehouse> getAllWarehouses();
    WarehouseDto getWarehouseById(Long id);
    void addWarehouse(Warehouse warehouse);
    void updateWarehouse(Warehouse warehouse);
    boolean warehouseExists(Long id);
    Warehouse createWarehouse(WarehouseDto warehouseDto); 
    Optional<Warehouse> updateWarehouse(Long id, WarehouseDto warehouseDto);
    List<ItemDto> getItemsByWarehouseId(Long warehouseId);
    void deleteWarehouse(Long warehouseId);
    Warehouse createWarehouseWithItems(WarehouseDto warehouseDto);
    List<WarehouseInfoDto> getAllWarehouseInfo();
    void exportWarehousesToExcel(HttpServletResponse response);
}
