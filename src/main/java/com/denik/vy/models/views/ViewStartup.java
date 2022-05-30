package com.denik.vy.models.views;

import com.denik.vy.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ViewStartup {
    @JsonProperty("id")
    public long id;
    @JsonProperty("username")
    public String username;
    @JsonProperty("email")
    public String email;
    @JsonProperty("rolesToString")
    public String rolesToString;
    @JsonProperty("isAdmin")
    public boolean isAdmin;
    @JsonProperty("rolesToList")
    public List<Role> roles;

    public ViewStartup(long id, String username, String email, String rolesToString, boolean isAdmin, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.roles = roles;
        this.rolesToString = rolesToString;
    }
}
