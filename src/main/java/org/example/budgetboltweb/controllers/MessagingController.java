package org.example.budgetboltweb.controllers;

import org.example.budgetboltweb.model.Chat;
import org.example.budgetboltweb.model.Review;
import org.example.budgetboltweb.model.User;
import org.example.budgetboltweb.repo.ChatRepo;
import org.example.budgetboltweb.repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
