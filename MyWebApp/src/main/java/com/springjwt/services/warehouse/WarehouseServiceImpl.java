package com.springjwt.services.warehouse;

import com.springjwt.dto.ItemDto;
import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Item;
 import java.io.IOException;
import com.springjwt.entities.Warehouse;
import com.springjwt.repositories.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements IWarehouseService {

  @Autowired
  private WarehouseRepository warehouseRepository;

  @Override
  public List<Warehouse> getAllWarehouses() {
    return warehouseRepository.findAllWarehouses();
  }

  @Override
  public WarehouseDto getWarehouseById(Long id) {
    Warehouse warehouse = warehouseRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + id));

    List<ItemDto> itemDtos = warehouse.getItems().stream()
        .map(item -> new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getQuantity()))
        .collect(Collectors.toList());

    return new WarehouseDto(
        warehouse.getId(),
        warehouse.getWarehouseName(),
        warehouse.getCreatedBy(),
        warehouse.getWarehouseDescription(),
        itemDtos);
  }

  @Override
  public void addWarehouse(Warehouse warehouse) {
    warehouseRepository.save(warehouse);
  }

  @Override
  public void updateWarehouse(Warehouse warehouse) {
    warehouseRepository.save(warehouse);
  }

  @Override
  public boolean warehouseExists(Long id) {
    return warehouseRepository.existsById(id);
  }

  @Override
  public Warehouse createWarehouse(WarehouseDto warehouseDto) {
    Warehouse warehouse = new Warehouse();
    warehouse.setWarehouseName(warehouseDto.getWarehouseName());
    warehouse.setWarehouseDescription(warehouseDto.getWarehouseDescription());

    return warehouseRepository.save(warehouse);

  }

  public Warehouse createWarehouseWithItems(WarehouseDto warehouseDto) {

    Warehouse warehouse = new Warehouse();
    warehouse.setWarehouseName(warehouseDto.getWarehouseName());
    warehouse.setWarehouseDescription(warehouseDto.getWarehouseDescription());
    warehouse.setCreatedBy(warehouseDto.getCreatedBy());

    List<Item> items = new ArrayList<>();

    for (ItemDto itemDto : warehouseDto.getItems()) {
      Item item = new Item();
      item.setName(itemDto.getName());
      item.setDescription(itemDto.getDescription());
      item.setQuantity(itemDto.getQuantity());
      item.setWarehouse(warehouse);
      items.add(item);
    }

    warehouse.setItems(items);

    return warehouseRepository.save(warehouse);
  }

  public Warehouse findWarehouseById(Long id) {
    return warehouseRepository.findById(id)
        .orElseThrow();
  }

  @Override
  public Optional<Warehouse> updateWarehouse(Long id, WarehouseDto warehouseDto) {
    return warehouseRepository.findById(id).map(existingWarehouse -> {
      existingWarehouse.setWarehouseName(warehouseDto.getWarehouseName());
      existingWarehouse.setWarehouseDescription(warehouseDto.getWarehouseDescription());
      return warehouseRepository.save(existingWarehouse);
    });
  }

  @Override
  public List<ItemDto> getItemsByWarehouseId(Long warehouseId) 
  {
    Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
    if (warehouse != null) {
      return warehouse.getItems().stream()
          .map(item -> new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getQuantity()))
          .collect(Collectors.toList());
    }
    return List.of();
  }

  @Override
  public void deleteWarehouse(Long warehouseId) {
    warehouseRepository.deleteSupplyDocument(warehouseId);
  }

  @Override
  public List<WarehouseInfoDto> getAllWarehouseInfo() {
    return warehouseRepository.findAll().stream()
        .map(this::mapToWarehouseInfoDto)
        .collect(Collectors.toList());
  }

  private WarehouseInfoDto mapToWarehouseInfoDto(Warehouse warehouse) {
    WarehouseInfoDto dto = new WarehouseInfoDto();
    dto.setId(warehouse.getId());
    dto.setName(warehouse.getWarehouseName());
    dto.setDescription(warehouse.getWarehouseDescription());
    dto.setItemsCount((long) warehouse.getItems().size());
    dto.setStatus(warehouse.getItems().isEmpty() ? "Empty" : "Available");
    return dto;
  }

  // for Excel
  @Override
  public void exportWarehousesToExcel(HttpServletResponse response) {
  
      List<Warehouse> warehouses = warehouseRepository.findAllWarehouses();
  
      Workbook workbook = new XSSFWorkbook(); 
      Sheet sheet = workbook.createSheet("Warehouses");
  
   
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("Warehouse Name");
      headerRow.createCell(1).setCellValue("Warehouse Description");
      headerRow.createCell(2).setCellValue("Item Name");
      headerRow.createCell(3).setCellValue("Quantity");
  

      int rowIdx = 1;
      for (Warehouse warehouse : warehouses) {
          for (Item item : warehouse.getItems()) {
              Row row = sheet.createRow(rowIdx++);
              row.createCell(0).setCellValue(warehouse.getWarehouseName());
              row.createCell(1).setCellValue(warehouse.getWarehouseDescription());
              row.createCell(2).setCellValue(item.getName());
              row.createCell(3).setCellValue(item.getQuantity());
          }
      }
  

      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment; filename=warehouses.xlsx");
  

      try (ServletOutputStream outputStream = response.getOutputStream()) {
          workbook.write(outputStream);
      } catch (IOException e) {
         
          e.printStackTrace();
          try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating Excel file.");
          } catch (IOException e1) {
            
            e1.printStackTrace();
          }
      } finally {
     
          try {
              workbook.close(); 
          } catch (IOException e) {
              e.printStackTrace(); 
          }
      }
  }
}  