package com.springjwt.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String warehouseName;

    @Column(nullable = true)
    private String warehouseDescription;

    @Column(name = "created_by", nullable = false)
    private String createdBy;
    

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        createdDateTime = LocalDateTime.now();
    }


   
}
