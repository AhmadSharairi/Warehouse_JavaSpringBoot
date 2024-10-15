package com.springjwt.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supply_documents")
public class SupplyDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documentName;

    @Column(nullable = false)
    private String documentSubject;


    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;


    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "status")
    private String status;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnore
    private Warehouse warehouse;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private Item item;

    
    @PrePersist
    protected void onCreate() {
        createdDateTime = LocalDateTime.now();
    }

    


}
