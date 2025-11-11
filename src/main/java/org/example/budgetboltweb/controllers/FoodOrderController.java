package org.example.budgetboltweb.controllers;

import org.example.budgetboltweb.model.Cuisine;
import org.example.budgetboltweb.model.FoodOrder;
import org.example.budgetboltweb.repo.CuisineRepo;
import org.example.budgetboltweb.repo.FoodOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodOrderController {
    @Autowired
    private FoodOrderRepo foodOrderRepo;
    @Autowired
    private CuisineRepo cuisineRepo;

    @GetMapping(value = "getByUserId")
    public Iterable<FoodOrder> getByUserId() {
        return foodOrderRepo.getFoodOrdersByBuyer_Id(5);
    }

    @GetMapping(value = "getAllCuisines")
    public Iterable<Cuisine> getAllCuisines() {
        return cuisineRepo.findAll();
    }
}
