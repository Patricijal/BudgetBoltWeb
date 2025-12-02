package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rating;
    private String text;
    @JsonIgnore
    @ManyToOne
    private BasicUser commentOwner;
    @JsonIgnore
    @ManyToOne
    private BasicUser feedbackUser;
    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;
    @JsonIgnore
    @OneToOne
    private FoodOrder order;
    @JsonIgnore
    @ManyToOne
    private Chat chat;

//    public Review(int rating, String text) {
//        this.rating = rating;
//        this.text = text;
//    }

    public Review(String text, BasicUser commentOwner, Chat chat) {
        this.text = text;
        this.commentOwner = commentOwner;
        this.chat = chat;
    }

    public Review(int rating, String text, BasicUser commentOwner, Restaurant restaurant) {
        this.rating = rating;
        this.text = text;
        this.commentOwner = commentOwner;
        this.restaurant = restaurant;
    }

//    @Override
//    public String toString() {
//        return "Rating: " + rating + " | Text: " + text;
//    }

    @Override
    public String toString() {
        if (chat != null) {
            // Chat message format
            return "[" + (commentOwner != null ? commentOwner.getLogin() : "Unknown") + "]: " + text;
        } else {
            // Review format
            return "[" + (commentOwner != null ? commentOwner.getLogin() : "Unknown") + "]: " +
                    "Rating: " + rating + " | Comment: " + text + (restaurant != null ? " | Restaurant: " + restaurant.getName() : "");
        }
    }
}
