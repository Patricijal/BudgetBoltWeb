package org.example.budgetboltweb.controllers;

import lombok.RequiredArgsConstructor;
import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.BasicUserRepo;
import org.example.budgetboltweb.repo.CuisineRepo;
import org.example.budgetboltweb.repo.FoodOrderRepo;
import org.example.budgetboltweb.repo.RestaurantRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final BasicUserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final CuisineRepo cuisineRepo;
    private final FoodOrderRepo orderRepo;

    public FoodOrder createOrder(FoodOrderController.CreateOrderRequest req) {

        BasicUser user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepo.findById(req.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<Cuisine> cuisines = new ArrayList<>();
        double totalPrice = 0;

        // Calculate total price and build cuisine list
        for (FoodOrderController.CreateOrderRequest.OrderItemDTO item : req.getItems()) {
            Cuisine cuisine = cuisineRepo.findById(item.getCuisineId())
                    .orElseThrow(() -> new RuntimeException("Cuisine not found"));
            cuisines.add(cuisine);
            totalPrice += cuisine.getPrice() * item.getQuantity();
        }

        // Apply restaurant discount
        double finalPrice = totalPrice * (1 - restaurant.getDiscount() / 100.0) * (1 - req.getBonusPoints() / 100.0);
        finalPrice = Math.round(finalPrice * 100.0) / 100.0;

        // Create order
        FoodOrder order = new FoodOrder();
        order.setName("Order for " + user.getName());
        order.setPrice(finalPrice);
        order.setBuyer(user);
        order.setFood(cuisines);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDateCreated(LocalDate.now());
        order.setDateUpdated(LocalDate.now());

        return orderRepo.save(order);
    }
}
