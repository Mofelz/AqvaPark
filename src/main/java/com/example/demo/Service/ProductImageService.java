package com.example.demo.Service;

import com.example.demo.models.Image;
import com.example.demo.models.Product;
import com.example.demo.repo.ImageRepository;
import com.example.demo.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    public void saveImageAndTariff(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Image image = saveImageEntity(file);
            imageRepository.save(image);
            product.setImage(image);
        }
        productRepository.save(product);
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
