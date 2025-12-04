package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends BasicUser{
    private String license;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate bDate;

    @JsonIgnore
    @OneToMany(mappedBy = "driver", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodOrder> myOrders;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @JsonIgnore
    @OneToMany(mappedBy = "driver", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chats;


    public Driver(String login, String password, String name, String surname, String phoneNumber, String address, String license, LocalDate bDate, VehicleType vehicleType) {
        super(login, password, name, surname, phoneNumber, address);
        this.license = license;
        this.bDate = bDate;
        this.vehicleType = vehicleType;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public LocalDate getBDate() {
        return bDate;
    }

    public void setBDate(LocalDate bDate) {
        this.bDate = bDate;
    }

    public List<FoodOrder> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<FoodOrder> myOrders) {
        this.myOrders = myOrders;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
