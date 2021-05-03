package com.example.shop.web;

import com.example.shop.service.UserService;
import com.example.shop.web.interceptors.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/role")
public class RoleController {

    private final UserService userService;

    @Autowired
    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @PageTitle("Change role")
    @GetMapping("/change")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showRoleChangePage(Model model) {

        if (!model.containsAttribute("usernameNotFound")) {
            model.addAttribute("usernameNotFound", false);
        }
        model.addAttribute("usernames", this.userService.getUsernames());

        return "role-change";
    }

    @PostMapping("/change")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeRole(@RequestParam String username, @RequestParam String role, RedirectAttributes redirectAttributes) {

        if (username.isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameNotFound", true);
            return "redirect:/role/change";
        }

        this.userService.changeRole(username, role.toUpperCase());

        return "redirect:/role/change";
    }

}
