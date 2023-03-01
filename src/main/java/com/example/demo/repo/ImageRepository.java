package com.example.demo.repo;

import com.example.demo.models.Image;
import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findImageByProduct(Product product);
}
