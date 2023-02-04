package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String datapos, price, usluga;

    public Ticket(String datapos, String price, String usluga) {
        this.datapos = datapos;
        this.price = price;
        this.usluga = usluga;
    }

    public Ticket() {
    }
}
