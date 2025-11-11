package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {
}
