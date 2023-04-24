package com.example.demo.repo;

import com.example.demo.models.Status;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<Status, Long> {
    Status findFirstByOrderByIdAsc();
}