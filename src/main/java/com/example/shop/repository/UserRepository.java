package com.example.shop.repository;

import com.example.shop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT u.username FROM User AS u WHERE u.roles.size = 1 OR u.roles.size = 2 ORDER BY u.username ASC")
    List<String> findAllUsernames();
}
