package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Дата не может быть пустой")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "Дата посещения не может быть прошлой")
    private Date datapos;

    @NotEmpty(message = "Поле не может быть пустым1")
    @Size(min = 1, max = 8, message = "От 1 до 8 символов")
    private String uslaga;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 3000,max = 20000, message = "Один пользователь может купить только 5 билетов")
    private Integer price;

//    @Range(max = 10, message = "Осталось мест ")
//    private Integer maxkolvovisitors = 10;

    @NotNull(message = "Кто-то все таки должен пойти веселиться")
    @Range(min = 1, max = 7, message = "Диапазон от 1 до 5")
    private Integer visitors;

    @NotNull(message = "Кто-то все таки должен пойти веселиться")
    @Range(min = 1, max = 5, message = "Диапазон от 1 до 5")
    private Integer adults;

    @NotNull(message = "Кто-то все таки должен пойти веселиться")
    @Range(min = 0, max = 5, message = "Диапазон от 1 до 5")
    private Integer children;

    @ManyToOne
    @JoinColumn(name = "uslugaid", referencedColumnName = "id")
    private Usluga usluga;



    private int views;

    @ManyToOne(optional = true)
    private User user;

    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    @ManyToOne
    @JoinColumn(name = "tariffId", referencedColumnName = "id")
    private Tarif tarif;

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    @ManyToOne
    @JoinColumn(name = "stocksId", referencedColumnName = "id")
    private Stocks stocks;

    public Post(Long id, Date datapos, String uslaga, Integer price,
                Integer visitors, Integer maxkolvovisitors,
                Integer adults,Integer children ) {
        this.visitors = visitors;
        this.price = price;
        this.id = id;
        this.datapos = datapos;
        this.uslaga = uslaga;
//        this.maxkolvovisitors = maxkolvovisitors;
        this.adults = adults;
        this.children = children;
    }

    public Post() {

    }

    public Integer getAdults() {return adults;}

    public void setAdults(Integer adults) {this.adults = adults;}

    public Integer getChildren() {return children;}

    public void setChildren(Integer children) {this.children = children;}



    public Usluga getUsluga() {
        return usluga;
    }

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatapos() {
        return datapos;
    }

    public void setDatapos(Date datapos) {
        this.datapos = datapos;
    }

    public String getUslaga() {
        return uslaga;
    }

    public void setUslaga(String uslaga) {
        this.uslaga = uslaga;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public Integer getMaxkolvovisitors() {
//        return maxkolvovisitors;
//    }
//
//    public void setMaxkolvovisitors(Integer maxkolvovisitors) {
//        this.maxkolvovisitors = maxkolvovisitors;
//    }
}