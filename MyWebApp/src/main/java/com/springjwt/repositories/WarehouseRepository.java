package com.springjwt.repositories;


import java.util.List; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.springjwt.entities.Warehouse;
import jakarta.transaction.Transactional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Modifying 
    @Transactional 
    @Query(value = "CALL DeleteWarehouse(:warehouseId)", nativeQuery = true)
    void deleteSupplyDocument(@Param("warehouseId") Long warehouseId);

    @Query(value = "CALL GetWarehouses()", nativeQuery = true)
    List<Warehouse> findAllWarehouses();
}
