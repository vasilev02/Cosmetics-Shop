package com.example.shop.service.impl;

import com.example.shop.model.entity.User;
import com.example.shop.model.service.userServiceModels.ProfileChangeImageServiceModel;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.service.userServiceModels.UserRegisterServiceModel;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.DemoUserDetailsService;
import com.example.shop.service.CloudinaryService;
import com.example.shop.service.RoleService;
import com.example.shop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final DemoUserDetailsService demoUserDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService, DemoUserDetailsService demoUserDetailsService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.demoUserDetailsService = demoUserDetailsService;
    }

    @PostConstruct
    private void initializeAdmin() {

        if (this.userRepository.count() == 0) {
            User root = new User();
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("Root1@"));
            root.setEmail("root@gmail.com");
            root.setImageUrl("https://res.cloudinary.com/defiefioi/image/upload/v1618678122/blank-profile-picture-973460_640_lil14i.png");
            root.setRoles(roleService.findAllRoles());
            this.userRepository.saveAndFlush(root);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin1@"));
            admin.setEmail("admin@gmail.com");
            admin.setImageUrl("https://res.cloudinary.com/defiefioi/image/upload/v1618678122/blank-profile-picture-973460_640_lil14i.png");
            admin.setRoles(roleService.findAllRolesForAdmin());
            this.userRepository.saveAndFlush(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("User1@"));
            user.setEmail("user@gmail.com");
            user.setImageUrl("https://res.cloudinary.com/defiefioi/image/upload/v1618678122/blank-profile-picture-973460_640_lil14i.png");
            user.setRoles(roleService.findRoleByRole("USER"));
            this.userRepository.saveAndFlush(user);

        }
    }

    @Override
    public UserRegisterServiceModel registerAndLoginUser(UserRegisterServiceModel serviceModel) {
        User user = this.modelMapper.map(serviceModel, User.class);
        user.setImageUrl("https://res.cloudinary.com/defiefioi/image/upload/v1618678122/blank-profile-picture-973460_640_lil14i.png");
        user.setPassword(passwordEncoder.encode(serviceModel.getPassword()));
        user.setRoles(this.roleService.findRoleByRole("USER"));
        this.userRepository.saveAndFlush(user);

        UserDetails principal = demoUserDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                principal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return serviceModel;
    }

    @Override
    public boolean checkUsernameIfExists(String username) {
        return this.userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean checkEmailIfExists(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public List<String> getUsernames() {
        List<String> allUsernames = this.userRepository.findAllUsernames();
        return allUsernames;
    }

    @Override
    public void changeRole(String username, String role) {

        Optional<User> user = this.userRepository.findUserByUsername(username);

        if (role.equals("ADMIN")) {
            user.get().setRoles(this.roleService.findAllRolesForAdmin());
        } else {
            user.get().setRoles(this.roleService.findRoleByRole("USER"));
        }
        this.userRepository.saveAndFlush(user.get());
    }

    @Override
    public UserProfileServiceModel getUserByUsername(String name) {
        Optional<User> user = this.userRepository.findUserByUsername(name);
        return this.modelMapper.map(user.get(), UserProfileServiceModel.class);
    }

    @Async
    @Override
    public void deleteProfileById(String id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User getUserEntityByUsername(String name) {
        return this.userRepository.findUserByUsername(name).get();
    }

    @Override
    public void changeProfilePicture(ProfileChangeImageServiceModel serviceModel, String name) throws IOException {
        User user = this.userRepository.findUserByUsername(name).get();

        MultipartFile image = serviceModel.getImageUrl();
        String uploadImage = this.cloudinaryService.uploadImage(image);
        user.setImageUrl(uploadImage);
        this.userRepository.saveAndFlush(user);
    }


}
