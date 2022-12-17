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
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String predmet;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 1, max = 30, message = "Диапазон от 1 до 30")
    private Integer kolvopredmet;

    public Inventory(Long id, String predmet, Integer kolvopredmet) {
        this.kolvopredmet = kolvopredmet;
        this.predmet = predmet;
        this.id = id;
    }
    public Inventory(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public Integer getKolvopredmet() {
        return kolvopredmet;
    }

    public void setKolvopredmet(Integer kolvopredmet) {
        this.kolvopredmet = kolvopredmet;
    }
}
