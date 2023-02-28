package com.example.demo.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 100, message = "От 1 до 100 символов")
    private String nametarif;

    @NotNull(message = "Поле не может быть пустым")
    @Range(min = 1, max = 1000, message = "Диапазон от 1 до 5")
    private Integer price;

    @NotNull(message = "Поле не может быть пустым")
    @Range(min = 1, max = 1000, message = "От 1 до 100 символов")
    private Integer weight;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageId")
    private Image image;

    @ManyToMany
    @JoinTable(name = "collecting", joinColumns = @JoinColumn(name = "tarifId"), inverseJoinColumns = @JoinColumn(name = "postId"))
    private List<Post> postList;

    public Tarif(String nametarif, Image image, Integer price, Integer weight) {
        this.nametarif = nametarif;
        this.image = image;
        this.price = price;
        this.weight = weight;
    }

    public Tarif() {}

    public Long getId() {
        return id;
    }

    public List<Post> getPostList() {return postList;}

    public void setPostList(List<Post> postList) {this.postList = postList;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getNametarif() {
        return nametarif;
    }

    public void setNametarif(String nametarif) {
        this.nametarif = nametarif;
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
}
