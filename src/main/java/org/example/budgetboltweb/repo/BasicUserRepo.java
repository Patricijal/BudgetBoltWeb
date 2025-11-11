package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepo extends JpaRepository<BasicUser, Integer> {
}
