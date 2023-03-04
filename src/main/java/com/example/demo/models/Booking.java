package com.example.demo.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Range(min = 1, max = 100, message = "Диапазон от 1 до 100")
    @NotNull
    private Integer tableNumber;

    private Date timeArrival;

    private Date timeDeparture;

    @ManyToOne(optional = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "snackbarId", referencedColumnName = "id")
    private Snackbar snackbar;

    @ManyToMany
    @JoinTable(name = "collect", joinColumns = @JoinColumn(name = "orderId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> productList;

    public Booking(Integer tableNumber, Date timeArrival, Date timeDeparture, User user, Snackbar snackbar) {
        this.tableNumber = tableNumber;
        this.timeArrival = timeArrival;
        this.timeDeparture = timeDeparture;
        this.user = user;
        this.snackbar = snackbar;
    }

    public Booking(){}


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

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}