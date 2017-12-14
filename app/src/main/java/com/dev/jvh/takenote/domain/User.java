package com.dev.jvh.takenote.domain;

/**
 * Created by JochemVanHespen on 12/12/2017.
 */

public class User {
    private String name;
    private String email;
    private boolean isPremium;

    public User (String name, String email)
    {
        this.name = name;
        this.email = email;
        this.isPremium = false;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getEmail() {
        return email;
    }
}
