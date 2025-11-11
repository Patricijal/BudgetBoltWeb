package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepo extends JpaRepository<Driver, Integer> {
}
