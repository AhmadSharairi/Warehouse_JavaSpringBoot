package com.springjwt.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.springjwt.dto.ItemDto;
import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Item;
import com.springjwt.entities.Warehouse;
import com.springjwt.repositories.ItemRepository;

import com.springjwt.services.warehouse.IWarehouseService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
// @PreAuthorize("hasRole('Manager')")
@RequestMapping("/api/warehouses")
public class WarehouseController {

  @Autowired
  private IWarehouseService warehouseService;
  private final ModelMapper modelMapper;

  @Autowired
  private ItemRepository itemRepo;


  public WarehouseController(ModelMapper modelMapper) {

    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<WarehouseDto>> getAllWarehouses() {

    List<Warehouse> warehouses = warehouseService.getAllWarehouses();

    List<WarehouseDto> warehouseDtos = warehouses.stream()
        .map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class))
        .toList();

    return ResponseEntity.ok(warehouseDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable Long id) 
  {
    WarehouseDto warehouse = warehouseService.getWarehouseById(id);
    return ResponseEntity.ok(warehouse);
  }

  @PostMapping
  public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseDto warehouseDto) 
  {
    Warehouse warehouse = warehouseService.createWarehouse(warehouseDto);
    return new ResponseEntity<>(warehouse, HttpStatus.OK);
  }

    @PostMapping("/withItems")
    public ResponseEntity<Warehouse> createWarehouseWithItems(@RequestBody WarehouseDto warehouseDto) {
        Warehouse createdWarehouse = warehouseService.createWarehouseWithItems(warehouseDto);
        return new ResponseEntity<>(createdWarehouse, HttpStatus.OK);
    }
    


  @PutMapping("/{id}")
  public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseDto warehouseDto) 
  {
    Optional<Warehouse> updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDto);
    return updatedWarehouse.map(ResponseEntity::ok)
        .orElseGet(() -> 
        ResponseEntity.notFound().build());
  }

  @GetMapping("/items/count")
  public ResponseEntity<Long> getTotalItemsCount() 
  {
    Long totalCount = itemRepo.count();
    return ResponseEntity.ok(totalCount);
  }


  @GetMapping("/{warehouseId}/items")
  public ResponseEntity<List<ItemDto>> getItemsByWarehouseId(@PathVariable Long warehouseId) {
    List<ItemDto> items = warehouseService.getItemsByWarehouseId(warehouseId);
    if (items.isEmpty()) {
        return ResponseEntity.notFound().build(); 
    }
    return ResponseEntity.ok(items); 
}


  @DeleteMapping("{warehouseId}")
  public ResponseEntity<Void> deleteSupplyDocument(@PathVariable Long warehouseId) {
    warehouseService.deleteWarehouse(warehouseId);
      return ResponseEntity.noContent().build(); 
  }
  

  @GetMapping("/info")
  public List<WarehouseInfoDto> getAllWarehouseInfo() 
  {
    return warehouseService.getAllWarehouseInfo();
  }


    @PostMapping("/export")
    @ResponseStatus(HttpStatus.OK)
    public void exportWarehousesToExcel(HttpServletResponse response) throws IOException {
        warehouseService.exportWarehousesToExcel(response);
    }
}
