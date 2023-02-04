package com.example.demo.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "ProfileInventory", joinColumns = @JoinColumn(name = "profileId"), inverseJoinColumns = @JoinColumn(name = "inventoryId"))
    private List<Profile> profileList;

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

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
