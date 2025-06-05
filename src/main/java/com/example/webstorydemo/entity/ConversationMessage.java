package com.example.webstorydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessage {

    private String content;
    private String mediaUrl;

    private Conversation conversation;
    private ConversationMember member;
}
