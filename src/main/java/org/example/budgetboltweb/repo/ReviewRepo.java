package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
    boolean existsByCommentOwnerIdAndOrderId(int commentOwnerId, int orderId);
}
