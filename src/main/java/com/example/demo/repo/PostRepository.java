package com.example.demo.repo;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findById(String id);
    Post findTopByTableNumberOrderByIdDesc(int tableNumber);
}

