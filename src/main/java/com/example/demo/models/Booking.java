package com.example.demo.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

//Модель заказа
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Range(min = 1, max = 100, message = "Диапазон от 1 до 100")
    @NotNull
    private Integer tableNumber;

    private boolean payment;

    private Date timeArrival;

    private Date timeDeparture;

    @ManyToOne(optional = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "statusId", referencedColumnName = "id")
    private Status status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.REMOVE)
    private List<Cart> carts;

    public Booking(Integer tableNumber, Date timeArrival, Date timeDeparture, User user,Status status) {
        this.tableNumber = tableNumber;
        this.timeArrival = timeArrival;
        this.timeDeparture = timeDeparture;
        this.user = user;
        this.status = status;
    }

    public Booking(){}

    public List<Cart> getCarts() {
        return carts;
    }
    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Date getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(Date timeArrival) {
        this.timeArrival = timeArrival;
    }

    public Date getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(Date timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}