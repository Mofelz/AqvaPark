package com.example.demo.repo;

import com.example.demo.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByNameCategory(String nameCategory);
}
