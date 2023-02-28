package com.example.demo.repo;

import com.example.demo.models.Image;
import com.example.demo.models.Tarif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findImageByTarif(Tarif tarif);
}
