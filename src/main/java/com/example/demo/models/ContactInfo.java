package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Email(message = "Некорректный ввод email")
    private String pochta;

    @Pattern(regexp = "[+]7[(]\\d{3}[)]\\d{3}-\\d{2}-\\d{2}",
            message = "Номера телефона должен состоять из 11 цифр и иметь данный формат: +7(999)999-99-99")
    private String phonenumber;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

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

