package org.example.budgetboltweb.controllers;

import org.example.budgetboltweb.model.Cuisine;
import org.example.budgetboltweb.model.FoodOrder;
import org.example.budgetboltweb.repo.CuisineRepo;
import org.example.budgetboltweb.repo.FoodOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodOrderController {
    @Autowired
    private FoodOrderRepo foodOrderRepo;
    @Autowired
    private CuisineRepo cuisineRepo;

    @GetMapping(value = "getByUserId/{id}")
    public @ResponseBody Iterable<FoodOrder> getByUserId(@PathVariable int id) {
        return foodOrderRepo.getFoodOrdersByBuyer_Id(id);
    }

    @GetMapping(value = "getAllCuisines")
    public Iterable<Cuisine> getAllCuisines() {
        return cuisineRepo.findAll();
    }
}
