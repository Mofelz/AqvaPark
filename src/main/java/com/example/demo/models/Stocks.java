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

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String nameSnackbar;

    public Stocks(Long id, String nameSnackbar) {
        this.id = id;
        this.nameSnackbar = nameSnackbar;
    }

    public Stocks() {}

    public String getNameSnackbar() {return nameSnackbar;}

    public void setNameSnackbar(String nameSnackbar) {this.nameSnackbar = nameSnackbar;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
