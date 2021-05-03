package com.example.shop.repository;

import com.example.shop.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findAll();

    List<Role> findRoleByRole(String role);
}
