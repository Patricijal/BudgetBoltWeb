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

    @GetMapping(value = "getAllChats")
    public @ResponseBody Iterable<Chat> getAllChats() {
        return chatRepo.findAll();
    }

    @GetMapping(value = "getAllReviews")
    public @ResponseBody Iterable<Review> getAllReviews() {
        return reviewRepo.findAll();
    }

//    @GetMapping(value = "getMessagesForOrder/{id}")
//    public @ResponseBody Iterable<Review> getMessagesForOrder(@PathVariable int id) {
//        return chatRepo.getChatByOrder_Id(id).getMessages();
//    }

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

//    @PostMapping("insertMessage/{chatId}")
//    public @ResponseBody Review insertMessage(@PathVariable int chatId, @RequestBody Review message) {
//
//        Chat chat = chatRepo.findById(chatId)
//                .orElseThrow(() -> new RuntimeException("Chat not found"));
//
//        // Attach message to chat
//        message.setChat(chat);
//
////        // Add message into chat's local list (optional but good for consistency)
////        chat.getMessages().add(message);
//
//        // Save message
//        Review saved = reviewRepo.save(message);
//
//        return saved;
//    }

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
        private int orderId;        // for client review
        private int commentOwnerId;
        private int feedbackUserId;
        private boolean driver;     // true if driver review
        private Integer chatId;     // required if driver review
    }

    @PostMapping("leaveReview")
    public Review leaveReview(@RequestBody ReviewDTO dto) {
        Review review = new Review();

        if (dto.isDriver()) {
            if (dto.getChatId() == null) {
                throw new RuntimeException("Chat ID required for driver review");
            }
            review.setChat(chatRepo.findById(dto.getChatId())
                    .orElseThrow(() -> new RuntimeException("Chat not found")));
        } else {
            FoodOrder order = foodOrderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            if (order.getOrderStatus() != OrderStatus.COMPLETED)
                throw new RuntimeException("Order not completed");
            review.setOrder(order);
        }

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
