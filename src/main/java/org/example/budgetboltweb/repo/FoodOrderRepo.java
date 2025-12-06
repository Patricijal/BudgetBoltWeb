package org.example.budgetboltweb.repo;

import org.example.budgetboltweb.model.FoodOrder;
import org.example.budgetboltweb.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepo extends JpaRepository<FoodOrder, Integer> {
    List<FoodOrder> getFoodOrdersByBuyer_Id(int id);

    List<FoodOrder> findByOrderStatus(OrderStatus orderStatus);

    Iterable<FoodOrder> getFoodOrdersByDriver_Id(int id);
}
