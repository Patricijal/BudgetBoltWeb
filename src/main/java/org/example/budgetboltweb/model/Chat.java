package org.example.budgetboltweb.model;

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
    @ManyToOne
    private BasicUser customer;
    @ManyToOne
    private Driver driver;
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private FoodOrder order;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> messages;

//    private LocalDateTime createdAt;
//    private LocalDateTime lastMessageAt;
//    private boolean isActive;

    public Chat(String name, FoodOrder order) {
        this.name = name;
        this.order = order;
        this.dateCreated = LocalDate.now();
        this.messages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Chat: " + name + " | Created on: " + dateCreated;
    }
}
