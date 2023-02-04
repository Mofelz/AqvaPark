package com.example.demo.repo;

import com.example.demo.models.Safers;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SafersRepository extends CrudRepository<Safers, Long> {
    List<Safers> findByFamiliya(String Familiya);
}
