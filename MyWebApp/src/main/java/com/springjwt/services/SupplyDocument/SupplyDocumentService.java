package com.springjwt.services.SupplyDocument;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.springjwt.dto.CreateSupplyDocumentDto;
import com.springjwt.entities.Item;
import com.springjwt.entities.SupplyDocument;
import com.springjwt.entities.Warehouse;
import com.springjwt.repositories.ItemRepository;
import com.springjwt.repositories.WarehouseRepository;
import com.springjwt.repositories.SupplyDocumentRepository;

@Service
public class SupplyDocumentService implements ISupplyDocumentService {

    @Autowired
    private SupplyDocumentRepository supplyDocumentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Warehouse findWarehouseById(Long id) 
    {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
    }

    @Override
    public Item findItemById(Long id) 
    {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public CreateSupplyDocumentDto saveSupplyDocument(CreateSupplyDocumentDto dto) 
    {

        Warehouse warehouse = findWarehouseById(dto.getWarehouseId());
        Item item = findItemById(dto.getItemId());

        SupplyDocument supplyDocument = SupplyDocumentMapper.toEntity(dto, warehouse, item);

        SupplyDocument savedDocument = supplyDocumentRepository.save(supplyDocument);

        return SupplyDocumentMapper.toDto(savedDocument);
    }

    @Override
    public List<SupplyDocument> getAllSupplyDocuments() 
    {
        return jdbcTemplate.query("CALL GetSupplyDocuments()", (rs, rowNum) -> {
            SupplyDocument supplyDocument = new SupplyDocument();

            supplyDocument.setId(rs.getLong("id"));
            supplyDocument.setDocumentName(rs.getString("document_name"));
            supplyDocument.setDocumentSubject(rs.getString("document_subject"));
            supplyDocument.setStatus(rs.getString("status"));
            supplyDocument.setCreatedDateTime(rs.getTimestamp("created_date_time").toLocalDateTime());

            supplyDocument.setCreatedBy(rs.getString("created_by"));

            return supplyDocument;
        });
    }


    @Override
    public void removeSupplyDocument(Long supplyDocumentId) {
        supplyDocumentRepository.deleteSupplyDocument(supplyDocumentId);
    }

    @Override
    public CreateSupplyDocumentDto updateSupplyDocument(Long supplyId, CreateSupplyDocumentDto supplyDocumentDto) 
    {
        SupplyDocument supplyDocument = supplyDocumentRepository.findById(supplyId)
                .orElseThrow();

        supplyDocument.setDocumentName(supplyDocumentDto.getName());
        supplyDocument.setDocumentSubject(supplyDocumentDto.getSubject());
        supplyDocument.setCreatedBy(supplyDocumentDto.getCreatedBy());
        supplyDocument.setStatus(supplyDocumentDto.getStatus());

        SupplyDocument updatedSupplyDocument = supplyDocumentRepository.save(supplyDocument);

        return SupplyDocumentMapper.toDto(updatedSupplyDocument);
    }

    
    public CreateSupplyDocumentDto updateSupplyDocumentStatus(Long supplyId, String status) 
    {
        SupplyDocument supplyDocument = supplyDocumentRepository.findById(supplyId)
                .orElseThrow(() -> new RuntimeException("SupplyDocument not found"));
    

        supplyDocument.setStatus(status);
        supplyDocumentRepository.save(supplyDocument);
    
  
        return SupplyDocumentMapper.toDto(supplyDocument);
    }
    
    public List<SupplyDocument> getSupplyDocumentsByCreated(String createdBy) {
        return supplyDocumentRepository.getSupplyDocumentsByCreated(createdBy);
    }
}
