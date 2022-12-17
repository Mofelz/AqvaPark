package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Не может быть пустой")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Future(message = "Дата посещения не может быть прошлой")
    private Date datapos;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 8, message = "От 1 до 8 символов")
    private String uslaga;

    @NotNull(message = "Не может быть пустым")
    @Range(min=500, max=3400, message = "Диапазон от 500 до 3400")
    private Integer price;

    @NotNull(message = "Не может быть пустым")
    @Range(min=1, max=5, message = "Диапазон от 1 до 5")
    private Integer visitors;



    private int views;

    public Profile(){}

    public Profile(Long id, Date datapos, String uslaga, Integer price, Integer visitors, int views) {
        this.id = id;
        this.datapos = datapos;
        this.uslaga = uslaga;
        this.price = price;
        this.visitors = visitors;
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatapos() {
        return datapos;
    }

    public void setDatapos(Date datapos) {
        this.datapos = datapos;
    }

    public String getUslaga() {
        return uslaga;
    }

    public void setUslaga(String uslaga) {
        this.uslaga = uslaga;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }
    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
