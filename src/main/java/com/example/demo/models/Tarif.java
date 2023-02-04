package com.example.demo.models;


import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Tarif {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 9, message = "От 1 до 8 символов")
    private String nametarif;

    public Tarif(Long id, String nametarif) {
        this.nametarif = nametarif;
//        this.price = price;
        this.id = id;
    }

    public Tarif() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNametarif() {
        return nametarif;
    }

    public void setNametarif(String nametarif) {
        this.nametarif = nametarif;
    }

//    public Integer getPrice() {
//        return price;
//    }
//
//    public void setPrice(Integer price) {
//        this.price = price;
//    }
}
