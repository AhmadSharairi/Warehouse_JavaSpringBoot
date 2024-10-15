package com.springjwt.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springjwt.dto.CreateSupplyDocumentDto;
import com.springjwt.entities.SupplyDocument;
import com.springjwt.services.SupplyDocument.SupplyDocumentService;

@RestController
@RequestMapping("/api/supplydocuments")
public class SupplyDocumentController {

  @Autowired
  private SupplyDocumentService supplyDocumentService;

  @PostMapping("/create")
  public ResponseEntity<CreateSupplyDocumentDto> createSupplyDocument(@RequestBody CreateSupplyDocumentDto supplyDocumentDto) {

    CreateSupplyDocumentDto savedDocument = supplyDocumentService.saveSupplyDocument(supplyDocumentDto);

    return ResponseEntity.status(201).body(savedDocument);
  }

  @PutMapping("/status/{supplyId}")
  public ResponseEntity<CreateSupplyDocumentDto> changeSupplyDocumentStatus(
      @PathVariable Long supplyId, @RequestBody Map<String, String> requestBody) {

    String status = requestBody.get("status");
    CreateSupplyDocumentDto updatedDocument = supplyDocumentService.updateSupplyDocumentStatus(supplyId, status);
    return ResponseEntity.ok(updatedDocument);
  }



  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteSupplyDocument(@PathVariable Long id) {
      supplyDocumentService.removeSupplyDocument(id);
      return ResponseEntity.noContent().build(); 
  }
  
  @GetMapping("/all")
  public List<SupplyDocument> getAllSupplyDocuments() {
    return supplyDocumentService.getAllSupplyDocuments();
  }

  @GetMapping("/createdBy/{createdBy}")
  public List<SupplyDocument> getSupplyDocuments(@PathVariable String createdBy) {
      return supplyDocumentService.getSupplyDocumentsByCreated(createdBy);
  }

}
