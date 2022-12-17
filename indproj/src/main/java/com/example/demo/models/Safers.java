package com.example.demo.models;

import jdk.jfr.Timespan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Safers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String familiya;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String imya;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String tochka;


    @NotEmpty(message = "Поле не может быть пустым")
    @Timespan()
    private String vremyaohrani;


    public Safers(Long id, String familiya, String imya, String tochka, String vremyaohrani) {
        this.id = id;
        this.familiya = familiya;
        this.imya = imya;
        this.tochka = tochka;
        this.vremyaohrani = vremyaohrani;
    }

    public Safers() {}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getFamiliya() {
        return familiya;
    }

    public void setFamiliya(String familiya) {
        this.familiya = familiya;
    }

    public String getImya() {
        return imya;
    }

    public void setImya(String imya) {
        this.imya = imya;
    }

    public String getTochka() {return tochka;}

    public void setTochka(String tochka) {this.tochka = tochka;}

    public String getVremyaohrani() {return vremyaohrani;}

    public void setVremyaohrani(String vremyaohrani) {this.vremyaohrani = vremyaohrani;}
}
