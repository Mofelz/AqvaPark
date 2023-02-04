package com.example.demo.repo;

import com.example.demo.models.Usluga;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UslugaRepository extends CrudRepository<Usluga, Long> {
    List<Usluga> findByNameusluga(String nameusluga);
}
