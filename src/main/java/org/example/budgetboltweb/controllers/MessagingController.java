package org.example.budgetboltweb.controllers;

import com.google.gson.Gson;
import lombok.Data;
import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
public class MessagingController {
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private FoodOrderRepo foodOrderRepo;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;

    @GetMapping(value = "getAllChats")
    public @ResponseBody Iterable<Chat> getAllChats() {
        return chatRepo.findAll();
    }

    @GetMapping(value = "getAllReviews")
    public @ResponseBody Iterable<Review> getAllReviews() {
        return reviewRepo.findAll();
    }


    @GetMapping(value = "getMessagesForOrder/{id}")
    public @ResponseBody Iterable<Review> getMessagesForOrder(@PathVariable int id) {
        Chat chat = chatRepo.getChatByOrder_Id(id);
        if(chat == null){
            FoodOrder order = foodOrderRepo.getReferenceById(id);
            Chat chat1 = new Chat("User " + order.getBuyer().getLogin(), "Chat order" + id, order);
            order.setChat(chat1);
            chatRepo.save(chat1);
        }
        return chatRepo.getChatByOrder_Id(id).getMessages();
    }


    @PostMapping(value = "sendMessage")
    public @ResponseBody String sendMessage(@RequestBody String info) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var messageText = properties.getProperty("messageText");
        var commentOwner = basicUserRepo.getReferenceById(Integer.valueOf(properties.getProperty("userId")));
        var order = foodOrderRepo.getReferenceById(Integer.valueOf(properties.getProperty("orderId")));

        Review review = new Review(messageText, commentOwner, order.getChat());
        reviewRepo.save(review);

        return "test";

    }

    @Data
    public static class ReviewDTO {
        private int rating;
        private String text;

        private Integer orderId;
        private Integer chatId;
        private Integer restaurantId;

        private int commentOwnerId;
        private int feedbackUserId;

        private boolean driver;
    }


    @PostMapping("leaveReview")
    public Review leaveReview(@RequestBody ReviewDTO dto) {

        Review review = new Review();

        if (dto.getRestaurantId() != null) {
            System.out.println("Creating restaurant review...");

            Restaurant restaurant = restaurantRepo.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            review.setRestaurant(restaurant);
        }

        else if (dto.isDriver()) {
            Chat chat = chatRepo.findById(dto.getChatId())
                    .orElseThrow(() -> new RuntimeException("Chat not found"));
            review.setChat(chat);
        }

        else {
            FoodOrder order = foodOrderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (order.getOrderStatus() != OrderStatus.COMPLETED)
                throw new RuntimeException("Order not completed");

            review.setOrder(order);
        }

        // Common user fields
        User owner = userRepo.findById(dto.getCommentOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner user not found"));

        User target = userRepo.findById(dto.getFeedbackUserId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        review.setCommentOwner((BasicUser) owner);
        review.setFeedbackUser((BasicUser) target);

        review.setRating(dto.getRating());
        review.setText(dto.getText());

        return reviewRepo.save(review);
    }

}
