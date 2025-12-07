package org.example.budgetboltweb.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.budgetboltweb.model.*;
import org.example.budgetboltweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
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


    @PostMapping(value = "validateUser") //http://localhost:8080/validateUser
    public @ResponseBody String getUserByCredentials(@RequestBody String info) {
        System.out.println(info);
        //?Kaip parsint
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var login = properties.getProperty("login");
        var rawPassword = properties.getProperty("password");
        User user = userRepo.findByLogin(login)
                .orElse(null);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userType", user.getClass().getSimpleName());
            jsonObject.addProperty("login", user.getLogin());
            jsonObject.addProperty("name", user.getName());
            jsonObject.addProperty("surname", user.getSurname());
            jsonObject.addProperty("id", user.getId());
            return gson.toJson(jsonObject);
        }
        return null;
    }

    // reik visus laukus per body aprasyti (kitaip null error)
    @PutMapping(value = "updateUser")
    public BasicUser updateUser(@RequestBody Map<String, Object> payload) {
        Integer id = (Integer) payload.get("id");
        Integer bonusPoints = (Integer) payload.get("bonusPoints");

        BasicUser user = (BasicUser) userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBonusPoints(bonusPoints);
        user.setDateUpdated(LocalDateTime.now());

        return userRepo.save(user);
    }

    @PutMapping(value = "updateUserById/{id}")
    public @ResponseBody User updateUserById(@RequestBody User updatedUser, @PathVariable int id) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Common fields
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setDateUpdated(LocalDateTime.now());

        // Subclass-specific fields
        if (existingUser instanceof Driver && updatedUser instanceof Driver) {
            Driver existingDriver = (Driver) existingUser;
            Driver updatedDriver = (Driver) updatedUser;
            existingDriver.setAddress(updatedDriver.getAddress());
            existingDriver.setLicense(updatedDriver.getLicense());
            existingDriver.setBDate(updatedDriver.getBDate());
            existingDriver.setVehicleType(updatedDriver.getVehicleType());
        } else if (existingUser instanceof BasicUser && updatedUser instanceof BasicUser) {
            ((BasicUser) existingUser).setAddress(((BasicUser) updatedUser).getAddress());
        }

        return userRepo.save(existingUser);
    }


    @PostMapping(value = "insertUser")
    public @ResponseBody User createUser(@RequestBody User user) {
        userRepo.save(user);
        return userRepo.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    @PostMapping(value = "insertBasicUser")
    public @ResponseBody User createBasicUser(@RequestBody BasicUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return userRepo.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    @PostMapping(value = "insertDriver")
    public @ResponseBody User createDriver(@RequestBody Driver user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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


    @GetMapping(value = "/allRestaurants")
    public @ResponseBody Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }
}
