package com.springjwt.services.warehouse;

import com.springjwt.dto.ItemDto;
import com.springjwt.dto.WarehouseDto;
import com.springjwt.dto.WarehouseInfoDto;
import com.springjwt.entities.Item;

import com.springjwt.entities.Warehouse;
import com.springjwt.repositories.WarehouseRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Warehouse> getAllWarehouses() {
        return jdbcTemplate.query("CALL GetWarehouses()", (rs, rowNum) -> {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(rs.getLong("id"));
            warehouse.setWarehouseName(rs.getString("warehouse_name"));
            warehouse.setWarehouseDescription(rs.getString("warehouse_description"));
            return warehouse;
        });
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

        // Set the createdBy field with the current username
        warehouse.setCreatedBy("ahmadsh");

        return warehouseRepository.save(warehouse);

    }

    @Override
    public Warehouse createWarehouseWithItems(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(warehouseDto.getWarehouseName());
        warehouse.setWarehouseDescription(warehouseDto.getWarehouseDescription());
        warehouse.setCreatedBy("ahmadsh");

        List<Item> items = warehouseDto.getItems().stream()
                .map(itemDto -> {
                    Item item = new Item();
                    item.setName(itemDto.getName());
                    item.setQuantity(itemDto.getQuantity());

                    if (itemDto.getDescription() != null) {
                        item.setDescription(itemDto.getDescription());
                    } else {
                        item.setDescription("No description provided");
                    }

                    item.setWarehouse(warehouse);
                    return item;
                })
                .collect(Collectors.toList());

        warehouse.setItems(items);
        return warehouseRepository.save(warehouse);
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
    public List<String> getItemsByWarehouseId(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse != null) {
            return warehouse.getItems().stream()
                    .map(item -> String.format("Name: %s, Description: %s, Quantity: %d",
                            item.getName(), item.getDescription(), item.getQuantity()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    

    @Override
    public ResponseEntity<Warehouse> deleteWarehouse(Long warehouseId) {
        String sql = "CALL DeleteWarehouse(?)";
        int rowsAffected = jdbcTemplate.update(sql, warehouseId);
    
        if (rowsAffected > 0) {
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
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

}
