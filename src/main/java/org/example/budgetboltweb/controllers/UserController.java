package org.example.budgetboltweb.controllers;

import com.google.gson.Gson;
import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("getUserById/{id}")
    public @ResponseBody User getUserById(@PathVariable int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
    @PostMapping(value = "validateUser")
    public @ResponseBody User getUserByCredentials(@RequestBody String info) {
        System.out.println(info);
        // Kaip parsinti?
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var login = properties.getProperty("login");
        var psw = properties.getProperty("password");
        return userRepo.getUserByLoginAndPassword(login, psw);
    }

    // reik visus laukus per body aprasyti (kitaip null error)
    @PutMapping(value = "updateUser")
    public @ResponseBody User updateUser(@RequestBody User user) {
        userRepo.save(user);
        return userRepo.getReferenceById(user.getId());
    }

    @PutMapping(value = "updateUserById/{id}")
    public @ResponseBody User updateUserById(@RequestBody User updatedUser, @PathVariable int id) {
        // Fetch existing user
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update common fields
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        // Update subclass-specific fields
        if (existingUser instanceof BasicUser && updatedUser instanceof BasicUser) {
            ((BasicUser) existingUser).setAddress(((BasicUser) updatedUser).getAddress());
        } else if (existingUser instanceof Driver && updatedUser instanceof Driver) {
            Driver existingDriver = (Driver) existingUser;
            Driver updatedDriver = (Driver) updatedUser;
            existingDriver.setAddress(updatedDriver.getAddress());
            existingDriver.setLicense(updatedDriver.getLicense());
            existingDriver.setBDate(updatedDriver.getBDate());
            existingDriver.setVehicleType(updatedDriver.getVehicleType());
        }

        // Save and return updated user
        return userRepo.save(existingUser);
    }


    @PostMapping(value = "insertUser")
    public @ResponseBody User createUser(@RequestBody User user) {
        userRepo.save(user);
        return userRepo.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    @PostMapping(value = "insertBasicUser")
    public @ResponseBody User createBasicUser(@RequestBody BasicUser user) {
        userRepo.save(user);
        return userRepo.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    @PostMapping(value = "insertDriver")
    public @ResponseBody User createDriver(@RequestBody Driver user) {
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

//    @GetMapping(value = "getAllRestaurants")
//    public @ResponseBody Iterable<Restaurant> getAllRestaurants() {
//        return restaurantRepo.findAll();
//    }

    @GetMapping(value = "/allRestaurants")
    public @ResponseBody Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }
}
