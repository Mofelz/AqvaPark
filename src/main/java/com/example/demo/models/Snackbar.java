package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Snackbar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String nameSnackbar;

    public Snackbar(Long id, String nameSnackbar) {
        this.id = id;
        this.nameSnackbar = nameSnackbar;
    }

    public Snackbar() {}

    public String getNameSnackbar() {return nameSnackbar;}

    public void setNameSnackbar(String nameSnackbar) {this.nameSnackbar = nameSnackbar;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
