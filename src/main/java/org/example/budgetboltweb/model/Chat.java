package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String text;
    private LocalDate dateCreated;
    @JsonIgnore
    @ManyToOne
    private BasicUser customer;
    @JsonIgnore
    @ManyToOne
    private Driver driver;
    @JsonIgnore
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private FoodOrder order;
    @JsonIgnore
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> messages;

    public Chat(String name, FoodOrder order) {
        this.name = name;
        this.order = order;
        this.dateCreated = LocalDate.now();
        this.messages = new ArrayList<>();
    }

    public Chat(String name, String text, FoodOrder order) {
        this.name = name;
        this.text = text;
        this.dateCreated = LocalDate.now();
        this.order = order;
    }

    @Override
    public String toString() {
        return "Chat: " + name + " | Created on: " + dateCreated;
    }
}
