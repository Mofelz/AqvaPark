package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {

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
    @JoinColumn(name = "stocksId", referencedColumnName = "id")
    private Stocks stocks;

    public List<Tarif> getTarifList() {
        return tarifList;
    }

    public void setTarifList(List<Tarif> tarifList) {
        this.tarifList = tarifList;
    }

    @ManyToMany
    @JoinTable(name = "collecting", joinColumns = @JoinColumn(name = "postId"), inverseJoinColumns = @JoinColumn(name = "tarifId"))
    private List<Tarif> tarifList;

    public Post(Integer tableNumber, Date timeArrival, Date timeDeparture, User user, Stocks stocks) {
        this.tableNumber = tableNumber;
        this.timeArrival = timeArrival;
        this.timeDeparture = timeDeparture;
        this.user = user;
        this.stocks = stocks;
    }

    public Post(){}

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

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }
}