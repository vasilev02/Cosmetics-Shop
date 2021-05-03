package com.example.shop.unit.services;

import com.example.shop.model.entity.Role;
import com.example.shop.model.entity.User;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.service.userServiceModels.UserRegisterServiceModel;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.DemoUserDetailsService;
import com.example.shop.service.CloudinaryService;
import com.example.shop.service.RoleService;
import com.example.shop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private User user;
    private UserRegisterServiceModel userRegisterServiceModel;
    private Role userRole, adminRole, rootRole;

    private  UserServiceImpl serviceToTest;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    DemoUserDetailsService mockDemoUserDetailsService;

    @Mock
    CloudinaryService cloudinaryService;

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

        user = new User();
        user.setId("ID");
        user.setUsername("USERNAME");
        user.setEmail("EMAIL");
        user.setPassword("PASSWORD");
        user.setRoles(List.of(userRole,adminRole,rootRole));

        serviceToTest = new UserServiceImpl(mockUserRepository,mockRoleService, new ModelMapper(), mockPasswordEncoder, cloudinaryService, mockDemoUserDetailsService);

    }

    @Test
    public void testCheckUsernameIfExistsTrue() {
        Mockito.when(mockUserRepository.findUserByUsername("USERNAME")).thenReturn(Optional.of(user));
        boolean checkUsername = serviceToTest.checkUsernameIfExists("USERNAME");
        Assertions.assertTrue(checkUsername);
    }

    @Test
    public void testCheckUsernameIfExistsFalse() {
        Mockito.when(mockUserRepository.findUserByUsername("ANOTHER USERNAME")).thenReturn(Optional.empty());
        boolean checkUsername = serviceToTest.checkUsernameIfExists("ANOTHER USERNAME");
        Assertions.assertFalse(checkUsername);
    }

    @Test
    public void testCheckEmailIfExistsTrue() {
        Mockito.when(mockUserRepository.findUserByEmail("EMAIL")).thenReturn(Optional.of(user));
        boolean checkUsername = serviceToTest.checkEmailIfExists("EMAIL");
        Assertions.assertTrue(checkUsername);
    }

    @Test
    public void testCheckEmailIfExistsFalse() {
        Mockito.when(mockUserRepository.findUserByEmail("ANOTHER EMAIL")).thenReturn(Optional.empty());
        boolean checkUsername = serviceToTest.checkEmailIfExists("ANOTHER EMAIL");
        Assertions.assertFalse(checkUsername);
    }

    @Test
    public void testGetUsernames() {
        Mockito.when(mockUserRepository.findAllUsernames()).thenReturn(List.of("USERNAME"));
        List<String> usernames = serviceToTest.getUsernames();
        Assertions.assertEquals("USERNAME", usernames.get(0));
    }

    @Test
    public void testGetUsernamesSize() {
        Mockito.when(mockUserRepository.findAllUsernames()).thenReturn(List.of("USERNAME"));
        List<String> usernames = serviceToTest.getUsernames();
        Assertions.assertEquals(1, usernames.size());
    }

    @Test
    public void testGetUserByUsername() {
        Mockito.when(mockUserRepository.findUserByUsername("USERNAME")).thenReturn(Optional.of(user));
        UserProfileServiceModel user = serviceToTest.getUserByUsername("USERNAME");
        Assertions.assertEquals("ID", user.getId());
        Assertions.assertEquals("USERNAME", user.getUsername());
        Assertions.assertEquals("EMAIL", user.getEmail());
    }

    @Test
    public void testDeleteProfileById() {
        serviceToTest.deleteProfileById("ID");
        Mockito.verify(mockUserRepository, times(1)).deleteById("ID");
    }

    @Test
    public void testGetUserEntityByUsername() {
        Mockito.when(mockUserRepository.findUserByUsername("USERNAME")).thenReturn(Optional.of(user));
        User user = serviceToTest.getUserEntityByUsername("USERNAME");
        Assertions.assertEquals("ID", user.getId());
        Assertions.assertEquals("USERNAME", user.getUsername());
        Assertions.assertEquals("EMAIL", user.getEmail());
    }

}
