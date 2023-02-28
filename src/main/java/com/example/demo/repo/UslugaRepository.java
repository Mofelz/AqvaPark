package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UslugaRepository extends CrudRepository<Usluga, Long> {
    List<Usluga> findByNameusluga(String nameusluga);
}
