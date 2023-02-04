package com.example.demo.repo;

import com.example.demo.models.Schedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    List<Schedule> findById(String id);
}
