package org.example.budgetboltweb.controllers;

import com.google.gson.Gson;
import org.example.budgetboltweb.model.Chat;
import org.example.budgetboltweb.model.FoodOrder;
import org.example.budgetboltweb.model.Review;
import org.example.budgetboltweb.model.User;
import org.example.budgetboltweb.repo.BasicUserRepo;
import org.example.budgetboltweb.repo.ChatRepo;
import org.example.budgetboltweb.repo.FoodOrderRepo;
import org.example.budgetboltweb.repo.ReviewRepo;
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

}
