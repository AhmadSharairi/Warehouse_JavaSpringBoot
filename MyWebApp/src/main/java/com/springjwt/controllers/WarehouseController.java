package com.springjwt.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Warehouse;
import com.springjwt.repositories.ItemRepository;

import com.springjwt.services.warehouse.IWarehouseService;

import java.util.List;
import java.util.Optional;

@RestController
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
    return new ResponseEntity<>(warehouse, HttpStatus.CREATED);
  }

  @PostMapping("/withItems")
  public ResponseEntity<Warehouse> createWarehouseWithItems(@RequestBody WarehouseDto warehouseDto) 
  {
    Warehouse warehouse = warehouseService.createWarehouseWithItems(warehouseDto);
    return new ResponseEntity<>(warehouse, HttpStatus.CREATED);
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
  public ResponseEntity<List<String>> getItemsByWarehouseId(@PathVariable Long warehouseId) 
  {
    List<String> items = warehouseService.getItemsByWarehouseId(warehouseId);
    return items.isEmpty() ? 
    ResponseEntity.notFound().build() :
     ResponseEntity.ok(items);
  }

  @DeleteMapping("/{warehouseId}")
  public ResponseEntity<Warehouse> deleteWarehouse(@PathVariable Long warehouseId)
   {
    return warehouseService.deleteWarehouse(warehouseId);
  }

  @GetMapping("/info")
  public List<WarehouseInfoDto> getAllWarehouseInfo() 
  {
    return warehouseService.getAllWarehouseInfo();
  }

}
