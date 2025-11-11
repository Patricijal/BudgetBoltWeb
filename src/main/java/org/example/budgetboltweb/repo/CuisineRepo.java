package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepo extends JpaRepository<Cuisine, Integer> {
}
