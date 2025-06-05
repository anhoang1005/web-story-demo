package com.example.webstorydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private String name;
    private String avatar;
    private String memberCount;
    private String lastMessage;
    private String sendLastAt;

    private List<ConversationMessage> messageList = new ArrayList<>();
    private List<ConversationMember> memberList = new ArrayList<>();
}
