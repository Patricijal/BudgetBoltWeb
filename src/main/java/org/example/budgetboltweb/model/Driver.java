package org.example.budgetboltweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends BasicUser{
    private String license;
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
}
