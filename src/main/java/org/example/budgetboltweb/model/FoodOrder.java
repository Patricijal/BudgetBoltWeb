package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JoinTable(
            name = "foodorder_cuisine",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
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

    @JsonProperty("buyerId")
    public int getBuyerId() {
        return buyer != null ? buyer.getId() : 0;
    }

    @JsonProperty("driverId")
    public int getDriverId() {
        return driver != null ? driver.getId() : 0;
    }

    @Override
    public String toString() {
        return name + ", " + price + " EUR";
    }
}
