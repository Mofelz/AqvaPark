package com.example.demo.repo;

import com.example.demo.models.Tarif;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TarifRepository extends CrudRepository<Tarif, Long> {
    List<Tarif> findByNametarif(String nametarif);
}
