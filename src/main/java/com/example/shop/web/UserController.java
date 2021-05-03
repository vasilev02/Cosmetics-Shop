package com.example.shop.web;

import com.example.shop.model.binding.ProfileChangeImageBindingModel;
import com.example.shop.model.binding.UserRegisterBindingModel;
import com.example.shop.model.service.userServiceModels.ProfileChangeImageServiceModel;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.service.userServiceModels.UserRegisterServiceModel;
import com.example.shop.model.view.OrderProfileViewModel;
import com.example.shop.model.view.UserProfileViewModel;
import com.example.shop.service.OrderService;
import com.example.shop.service.UserService;
import com.example.shop.web.interceptors.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, OrderService orderService, ModelMapper modelMapper) {
        this.userService = userService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PageTitle("Login")
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLoginPage() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping("/login-error")
    @PreAuthorize("isAnonymous()")
    public String failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                      String username, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        redirectAttributes.addFlashAttribute("bad_credentials", true);
        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:/users/login";
    }

    @PageTitle("Register")
    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String showRegisterPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            if (!model.containsAttribute("userRegisterBindingModel")) {
                model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
                model.addAttribute("passwords_error", false);
                model.addAttribute("usernameExistsError", false);
                model.addAttribute("emailExistsError", false);
            }
            return "register";
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String registerAndLoginUser(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (this.userService.checkUsernameIfExists(userRegisterBindingModel.getUsername())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("usernameExistsError", true);
            return "redirect:/users/register";
        }

        if (this.userService.checkEmailIfExists(userRegisterBindingModel.getEmail())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("emailExistsError", true);
            return "redirect:/users/register";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:/users/register";
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("passwords_error", true);
            return "redirect:/users/register";
        }

        UserRegisterServiceModel serviceModel = this.modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class);
        this.userService.registerAndLoginUser(serviceModel);
        return "redirect:/";
    }

    @PageTitle("Profile")
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String showProfilePage(Principal principal, Model model) {

        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(principal.getName());
        UserProfileViewModel viewModel = this.modelMapper.map(serviceModel, UserProfileViewModel.class);

        List<OrderProfileViewModel> orderViewModels = this.orderService.getAllUserOrdersByUsername(principal.getName()).stream().map(order -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String date = order.getCreatedOn();
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            String formattedString = localDateTime.format(formatter);
            order.setCreatedOn(formattedString);

            return this.modelMapper.map(order, OrderProfileViewModel.class);
        }).collect(Collectors.toList());

        model.addAttribute("user", viewModel);
        model.addAttribute("orders", orderViewModels);
        return "profile";
    }

    @GetMapping("/profile/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProfile(@PathVariable String id) {
        SecurityContextHolder.clearContext();
        this.userService.deleteProfileById(id);
        return "redirect:/";
    }

    @PostMapping("/profile/picture")
    @PreAuthorize("isAuthenticated()")
    public String changeProfilePicture(ProfileChangeImageBindingModel profileChangeImageBindingModel, RedirectAttributes redirectAttributes,
                                       Principal principal) throws IOException {
        if(profileChangeImageBindingModel.getImageUrl().getSize() == 0){
            return "redirect:/users/profile";
        }
        ProfileChangeImageServiceModel serviceModel = this.modelMapper.map(profileChangeImageBindingModel, ProfileChangeImageServiceModel.class);
        this.userService.changeProfilePicture(serviceModel, principal.getName());
        return "redirect:/users/profile";
    }

}