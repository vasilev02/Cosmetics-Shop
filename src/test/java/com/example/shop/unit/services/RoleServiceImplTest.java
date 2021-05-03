package com.example.shop.unit.services;


import com.example.shop.model.entity.Role;
import com.example.shop.repository.RoleRepository;
import com.example.shop.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    private Role userRole, adminRole, rootRole;

    private RoleServiceImpl serviceToTest;

    @Mock
    RoleRepository mockRoleRepository;

    @BeforeEach
    public void init() {

        userRole = new Role();
        userRole.setId("ID1");
        userRole.setRole("USER");

        adminRole = new Role();
        adminRole.setId("ID2");
        adminRole.setRole("ADMIN");

        rootRole = new Role();
        rootRole.setId("ID3");
        rootRole.setRole("ROOT");

        serviceToTest = new RoleServiceImpl(mockRoleRepository);
    }

    @Test
    public void testFindAll() {
        Mockito.when(mockRoleRepository.findAll()).thenReturn(List.of(userRole, adminRole, rootRole));
        List<Role> roles = serviceToTest.findAllRoles();
        Assertions.assertEquals(3, roles.size());
    }

    @Test
    public void testFindRoleByNameUser() {
        Mockito.when(mockRoleRepository.findRoleByRole("USER")).thenReturn(List.of(userRole));
        List<Role> roles = serviceToTest.findRoleByRole("USER");
        Assertions.assertEquals("ID1", roles.get(0).getId());
        Assertions.assertEquals("USER", roles.get(0).getRole());
    }

    @Test
    public void testFindRoleByNameAdmin() {
        Mockito.when(mockRoleRepository.findRoleByRole("ADMIN")).thenReturn(List.of(adminRole));
        List<Role> roles = serviceToTest.findRoleByRole("ADMIN");
        Assertions.assertEquals("ID2", roles.get(0).getId());
        Assertions.assertEquals("ADMIN", roles.get(0).getRole());
    }

    @Test
    public void testFindRoleByNameRoot() {
        Mockito.when(mockRoleRepository.findRoleByRole("ROOT")).thenReturn(List.of(rootRole));
        List<Role> roles = serviceToTest.findRoleByRole("ROOT");
        Assertions.assertEquals("ID3", roles.get(0).getId());
        Assertions.assertEquals("ROOT", roles.get(0).getRole());
    }
    
}
