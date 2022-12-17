package com.example.demo.repo;

import com.example.demo.models.Stocks;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface StockRepository extends CrudRepository<Stocks, Long> {
    List<Stocks> findById(String id);
}
