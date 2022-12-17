package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Usluga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 15, message = "От 1 до 15 символов")
    private String nameusluga;



    public String getNameusluga() {
        return nameusluga;
    }

    public void setNameusluga(String nameusluga) {
        this.nameusluga = nameusluga;
    }

    public Usluga(Long id, String nameusluga) {
        this.nameusluga = nameusluga;
        this.id = id;
    }

    public Usluga() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsluga() {
        return nameusluga;
    }

    public void setUsluga(String nameusluga) {
        this.nameusluga = nameusluga;
    }
}
