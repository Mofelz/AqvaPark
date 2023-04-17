package com.example.demo.repo;

import com.example.demo.models.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, Long> {
    List<Status> findByNameStatus(String nameStatus);
}