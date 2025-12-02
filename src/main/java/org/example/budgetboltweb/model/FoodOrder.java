package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double price;
    @JsonIgnore
    @ManyToOne
    private BasicUser buyer;
    @JsonIgnore
    @ManyToMany
    private List<Cuisine> food;
    @JsonIgnore
    @ManyToOne
    private Driver driver;
    @JsonIgnore
    @OneToOne
    private Chat chat;
    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

//    public FoodOrder(String name, Double price, BasicUser buyer, Restaurant restaurant) {
//        this.name = name;
//        this.price = price;
//        this.buyer = buyer;
//        this.restaurant = restaurant;
//    }

    public FoodOrder(String name, Double price, BasicUser buyer, List<Cuisine> food, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.food = food;
        this.restaurant = restaurant;
        this.orderStatus = OrderStatus.PENDING;
        this.dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        return name + ", " + price + " EUR";
    }
}
