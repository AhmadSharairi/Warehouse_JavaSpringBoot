package com.springjwt.services.SupplyDocument;

import java.util.List;

import com.springjwt.dto.CreateSupplyDocumentDto;
import com.springjwt.entities.Item;
import com.springjwt.entities.SupplyDocument;
import com.springjwt.entities.Warehouse;

public interface ISupplyDocumentService {

    Warehouse findWarehouseById(Long id);
    Item findItemById(Long id);
    CreateSupplyDocumentDto saveSupplyDocument(CreateSupplyDocumentDto dto);
     List<SupplyDocument> getAllSupplyDocuments();
     CreateSupplyDocumentDto updateSupplyDocument(Long supplyId, CreateSupplyDocumentDto supplyDocumentDto);
     void removeSupplyDocument(Long supplyDocumentId);


    

}
