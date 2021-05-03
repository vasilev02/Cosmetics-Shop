package com.example.shop.service.impl;

import com.example.shop.model.entity.Role;
import com.example.shop.repository.RoleRepository;
import com.example.shop.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @PostConstruct
    private void initializeRoles() {

        if (this.roleRepository.count() == 0) {
            Role root = new Role("ROOT");
            Role admin = new Role("ADMIN");
            Role user = new Role("USER");

            this.roleRepository.saveAndFlush(root);
            this.roleRepository.saveAndFlush(admin);
            this.roleRepository.saveAndFlush(user);
        }
    }

    @Override
    public List<Role> findAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public List<Role> findAllRolesForAdmin() {
        List<Role> roles = findRoleByRole("USER");
        roles.add(findRoleByRole("ADMIN").get(0));
        return roles;
    }

    @Override
    public List<Role> findRoleByRole(String role) {
        return this.roleRepository.findRoleByRole(role);
    }
}
