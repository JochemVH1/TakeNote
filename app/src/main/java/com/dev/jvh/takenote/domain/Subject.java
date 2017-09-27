package com.dev.jvh.takenote.domain;

import java.io.Serializable;

/**
 * Created by JochemVanHespen on 9/19/2017.
 */

public class Subject implements Serializable {

    private String title;
    private String description;

    public Subject (String title){
        this.title = title;
    }
    public Subject (String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription() { return description; }
}
