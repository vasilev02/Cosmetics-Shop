package com.example.shop.repository;

import com.example.shop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findProductByNameAndProducer(String name, String producer);

    @Query(value = "SELECT p FROM Product AS p ORDER BY p.addedOn DESC")
    List<Product> findProductByAddedOnOrderByAddedOnIdDesc();

    Optional<Product> findProductById(String id);
}
