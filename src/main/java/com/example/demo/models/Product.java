package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 100, message = "От 1 до 100 символов")
    private String nameProduct;

    @NotNull(message = "Поле не может быть пустым")
    @Range(min = 1, max = 1000, message = "Диапазон от 1 до 5")
    private Integer price;

    @NotNull(message = "Поле не может быть пустым")
    @Range(min = 1, max = 1000, message = "От 1 до 100 символов")
    private Integer weight;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageId")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;

    public Product(String nameProduct, Image image, Integer price, Integer weight, Category category) {
        this.nameProduct = nameProduct;
        this.image = image;
        this.price = price;
        this.weight = weight;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getPrice() {return price;}

    public void setPrice(Integer price) {this.price = price;}

    public Integer getWeight() {return weight;}

    public void setWeight(Integer weight) {this.weight = weight;}

    public String getNameProduct() {return nameProduct;}

    public void setNameProduct(String nameProduct) {this.nameProduct = nameProduct;}
}
