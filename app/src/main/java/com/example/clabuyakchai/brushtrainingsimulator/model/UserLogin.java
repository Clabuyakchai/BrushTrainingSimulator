package com.example.clabuyakchai.brushtrainingsimulator.model;

import java.io.Serializable;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class UserLogin {
    private String username;
    private String password;

    public UserLogin(){

    }

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
