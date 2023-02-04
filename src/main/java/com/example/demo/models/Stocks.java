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
public class Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 1, max = 5, message = "Диапазон от 1 до 5")
    private Integer value;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 2400, max = 3300, message = "Диапазон от 2400 до 3300")
    private Integer price;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 2000, max = 3300, message = "Диапазон от 2400 до 3300")
    private Integer itogprice;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 8, message = "От 1 до 8 символов")
    private String namestock;

    public Stocks(Long id, Integer value,Integer itogprice, Integer price, String namestock) {
        this.value = value;
        this.id = id;
        this.itogprice = itogprice;
        this.price = price;
        this.namestock = namestock;
    }

    public Stocks() {

    }

    public String getNamestock() {
        return namestock;
    }

    public void setNamestock(String namestock) {
        this.namestock = namestock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {return value;}

    public void setValue(Integer value) {this.value = value;}

    public Integer getItogprice() {return itogprice;}

    public void setItogprice(Integer itogprice) {this.itogprice = itogprice;}

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
