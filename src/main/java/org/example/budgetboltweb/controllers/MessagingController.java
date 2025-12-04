package org.example.budgetboltweb.controllers;

import org.example.budgetboltweb.model.Chat;
import org.example.budgetboltweb.model.Review;
import org.example.budgetboltweb.model.User;
import org.example.budgetboltweb.repo.ChatRepo;
import org.example.budgetboltweb.repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessagingController {
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private ReviewRepo reviewRepo;

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
        return chatRepo.getChatByOrder_Id(id).getMessages();
    }

    @PostMapping("insertMessage/{chatId}")
    public @ResponseBody Review insertMessage(@PathVariable int chatId, @RequestBody Review message) {

        Chat chat = chatRepo.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        // Attach message to chat
        message.setChat(chat);

//        // Add message into chat's local list (optional but good for consistency)
//        chat.getMessages().add(message);

        // Save message
        Review saved = reviewRepo.save(message);

        return saved;
    }



}
