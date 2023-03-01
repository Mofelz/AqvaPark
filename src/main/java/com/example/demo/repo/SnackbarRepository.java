package com.example.demo.repo;

import com.example.demo.models.Snackbar;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SnackbarRepository extends CrudRepository<Snackbar, Long> { }
