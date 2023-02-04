package com.example.demo.repo;

import com.example.demo.models.Inventory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    List<Inventory> findByPredmet(String predmet);
}
