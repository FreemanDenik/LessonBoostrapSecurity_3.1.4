package com.denik.vy.models.views;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;


public class ViewUser {
    public long id;
    public String username;
    public String email;
    public String roles;

    public ViewUser(long id, String username, String email, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
