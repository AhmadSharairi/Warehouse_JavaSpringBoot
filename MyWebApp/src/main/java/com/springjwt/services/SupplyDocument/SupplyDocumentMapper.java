package com.springjwt.services.SupplyDocument;



import com.springjwt.dto.CreateSupplyDocumentDto;
import com.springjwt.entities.SupplyDocument;
import com.springjwt.entities.Item;
import com.springjwt.entities.Warehouse;

public class SupplyDocumentMapper {

    public static SupplyDocument toEntity(CreateSupplyDocumentDto dto, Warehouse warehouse, Item item) {
        SupplyDocument supplyDocument = new SupplyDocument();
        supplyDocument.setDocumentName(dto.getName());
        supplyDocument.setDocumentSubject(dto.getSubject());
        supplyDocument.setCreatedBy(dto.getCreatedBy());
        supplyDocument.setStatus(dto.getStatus());
        supplyDocument.setWarehouse(warehouse);
        supplyDocument.setItem(item);
        return supplyDocument;
    }

    public static CreateSupplyDocumentDto toDto(SupplyDocument supplyDocument) {
        return new CreateSupplyDocumentDto(
            supplyDocument.getDocumentName(),
            supplyDocument.getDocumentSubject(),
            supplyDocument.getCreatedBy(),
            supplyDocument.getStatus(),
            supplyDocument.getWarehouse().getId(),
            supplyDocument.getItem().getId()
        );
    }
}
