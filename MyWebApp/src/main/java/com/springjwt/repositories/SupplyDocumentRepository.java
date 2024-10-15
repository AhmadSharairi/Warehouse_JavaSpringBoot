package com.springjwt.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springjwt.entities.SupplyDocument;

import jakarta.transaction.Transactional;



@Repository
public interface SupplyDocumentRepository extends JpaRepository<SupplyDocument,Long> 
{
    @Modifying 
    @Transactional 
    @Query(value = "CALL DeleteSupplyDocument(:supplyDocumentId)", nativeQuery = true)
    void deleteSupplyDocument(@Param("supplyDocumentId") Long supplyDocumentId);


    @Query(value = "CALL getSupplyDocumentsByCreated(:createdBy)", nativeQuery = true)
    List<SupplyDocument> getSupplyDocumentsByCreated(@Param("createdBy") String createdBy);

 
}
