package com.denik.vy.controllers;


import com.denik.vy.models.Role;
import com.denik.vy.models.User;
import com.denik.vy.models.views.ViewStartup;
import com.denik.vy.models.views.ViewUser;
import com.denik.vy.services.RoleService;
import com.denik.vy.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {
    final UserService userService;
    final RoleService roleService;

    public ApiController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/api/startup")
    public ViewStartup admin(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return new ViewStartup(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(t -> t.getName().substring(5)).collect(Collectors.joining(", ")),
                userService.userIsRoles(user.getUsername(), "ADMIN"),
                roleService.rolesWithoutPrefix());
    }


    @GetMapping("/api/admin/users")
    public List<ViewUser> users() {
        return userService.viewUsers();
    }
    @GetMapping("/api/profile")
    public ViewUser profile(@RequestParam(name = "id") long userId) {
        return userService.viewUser(userId);
    }

    @PostMapping("/api/admin/edit")
    public void edit(User user) {
        userService.edit(user);
    }

    @PostMapping("/api/admin/delete")
    public void delete(long id) {
        userService.delete(id);
    }
    @PostMapping("/api/admin/create")
    public void create(User user) {
        userService.create(user);
    }
}
