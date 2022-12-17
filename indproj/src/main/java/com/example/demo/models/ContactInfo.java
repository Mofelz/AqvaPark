package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 20, message = "От 1 до 20 символов")
    private String pochta;

    @NotNull(message = "Не может быть пустым")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7}$", message = "Некорретный ввод номера телефона")
    private String phonenumber;

    public ContactInfo(Long id, String pochta, String phonenumber) {
        this.id = id;
        this.pochta = pochta;
        this.phonenumber = phonenumber;
    }

    public ContactInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPochta() {
        return pochta;
    }

    public void setPochta(String pochta) {
        this.pochta = pochta;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
