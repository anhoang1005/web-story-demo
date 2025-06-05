package com.example.webstorydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMember {

    private Users users;
    private Conversation conversation;
    private Role role;

    public enum Role{
        ADMIN,
        USER
    }
}
