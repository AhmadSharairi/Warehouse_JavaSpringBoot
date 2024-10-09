package com.springjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springjwt.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


} 

