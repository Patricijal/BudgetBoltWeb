package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    User getUserByLoginAndPassword(String login, String password);

    Optional<User> findByLogin(String login);
}
