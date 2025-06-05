package com.example.webstorydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String title;
    private String description;
    private String isRead;
    private Type type;

    private Users users;


    public enum Type{
        SYSTEM,
        STORY
    }
}
