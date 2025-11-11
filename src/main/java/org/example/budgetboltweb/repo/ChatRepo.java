package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
}
