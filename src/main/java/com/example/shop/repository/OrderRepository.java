package com.example.shop.repository;

import com.example.shop.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT o FROM Order AS o WHERE o.finished = false ORDER BY o.createdOn DESC")
    List<Order> getAllOrders();

    Optional<Order> getOrderById(String id);

    @Query(value = "SELECT o FROM Order AS o WHERE o.username = :username ORDER BY o.createdOn DESC")
    List<Order> getAllByUsername(String username);

}
