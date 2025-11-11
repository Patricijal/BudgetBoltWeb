package org.example.budgetboltweb.controllers;

import com.google.gson.Gson;
import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

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

    //http://localhost:8080/validateUser?login=a&password=a
//    @GetMapping(value = "validateUser")
//    public @ResponseBody User getUserByCredentials(@RequestParam String login, @RequestParam String password) {
//        return userRepo.getUserByLoginAndPassword(login, password);
//    }

    //http://localhost:8080/validateUser su raw body
//    {
//        "login":"a",
//            "password":"a"
//    }
    @GetMapping(value = "validateUser")
    public @ResponseBody User getUserByCredentials(@RequestBody String info) {
        System.out.println(info);
        // Kaip parsinti?
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var login = properties.getProperty("login");
        var psw = properties.getProperty("password");
        return userRepo.getUserByLoginAndPassword(login, psw);
    }

    @PutMapping(value = "updateUser")
    public @ResponseBody User updateUser(@RequestBody User user) {
        userRepo.save(user);
        return userRepo.getReferenceById(user.getId());
    }

    @PutMapping(value = "updateUserById/{id}")
    public @ResponseBody User updateUserById(@RequestBody String info, @PathVariable int id) {
        User user = userRepo.findById(id).orElseThrow(()-> new RuntimeException()); // cia noriu savo alerto
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var name = properties.getProperty("name");
        user.setName(name);
        userRepo.save(user);
        return userRepo.getReferenceById(user.getId());
    }

    @PostMapping(value = "insertUser")
    public @ResponseBody User createUser(@RequestBody User user) {
        userRepo.save(user);
        return userRepo.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    @DeleteMapping(value = "deleteUser/{id}")
    public @ResponseBody String deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            return "Fail on delete";
        } else {
            return "Successful delete";
        }
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
