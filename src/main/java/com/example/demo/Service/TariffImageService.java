package com.example.demo.Service;

import com.example.demo.models.Image;
import com.example.demo.models.Tarif;
import com.example.demo.repo.ImageRepository;
import com.example.demo.repo.TarifRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TariffImageService {

    private final TarifRepository tarifRepository;

    private final ImageRepository imageRepository;

    public void saveImageAndTariff(Tarif tarif, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Image image = saveImageEntity(file);
            imageRepository.save(image);
            tarif.setImage(image);
        }
        tarifRepository.save(tarif);
    }

    private Image saveImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
