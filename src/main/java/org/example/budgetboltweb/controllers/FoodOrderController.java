package org.example.budgetboltweb.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.budgetboltweb.model.Cuisine;
import org.example.budgetboltweb.model.FoodOrder;
import org.example.budgetboltweb.repo.CuisineRepo;
import org.example.budgetboltweb.repo.FoodOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodOrderController {
    @Autowired
    private FoodOrderRepo foodOrderRepo;
    @Autowired
    private CuisineRepo cuisineRepo;
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "getByUserId/{id}")
    public @ResponseBody Iterable<FoodOrder> getByUserId(@PathVariable int id) {
        return foodOrderRepo.getFoodOrdersByBuyer_Id(id);
    }

    @GetMapping(value = "getAllCuisines")
    public @ResponseBody Iterable<Cuisine> getAllCuisines() {
        return cuisineRepo.findAll();
    }

    @GetMapping(value = "getMenuRestaurant/{id}")
    public Iterable<Cuisine> getRestaurantMenu(@PathVariable int id){
        return cuisineRepo.getCuisineByRestaurantId(id);
    }

    @Data
    public static class CreateOrderRequest {
        private int userId;
        private int restaurantId;
        private List<OrderItemDTO> items;

        @Data
        public static class OrderItemDTO {
            private int cuisineId;
            private int quantity;
        }
    }

    @PostMapping("createOrder")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {
        FoodOrder saved = orderService.createOrder(req);
        return ResponseEntity.ok(saved.getId());
    }
}
