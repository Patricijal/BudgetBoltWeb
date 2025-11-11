package org.example.budgetboltweb.controllers;

import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;

    @GetMapping(value = "getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping(value = "getByLoginAndPassword")
    public @ResponseBody User getByLoginAndPassword() {
        return userRepo.getUserByLoginAndPassword("a", "a");
    }

    @GetMapping(value = "getAllBasicUsers")
    public @ResponseBody Iterable<BasicUser> getAllBasicUsers() {
        return basicUserRepo.findAll();
    }

    @GetMapping(value = "getAllDrivers")
    public @ResponseBody Iterable<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }

    @GetMapping(value = "getAllRestaurants")
    public @ResponseBody Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }
}
