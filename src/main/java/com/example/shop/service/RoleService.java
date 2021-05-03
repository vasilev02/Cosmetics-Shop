package com.example.shop.service;

import com.example.shop.model.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();

    List<Role> findAllRolesForAdmin();

    List<Role> findRoleByRole(String role);
}
