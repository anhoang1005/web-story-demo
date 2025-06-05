package com.example.webstorydemo.mapper;

import com.example.webstorydemo.entity.StoryChapter;
import com.example.webstorydemo.entity.StoryComment;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.model.comment.CommentDto;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto entityToDto(StoryComment comment){
        StoryComment parent = comment.getParentComment();
        StoryChapter chapter = comment.getChapter();
        Users users = comment.getUsers();
        return CommentDto.builder()
                .commentId(comment.getId())
                .parentId(parent!=null ? parent.getId() : null)
                .username(users.getUsername())
                .avatar(users.getAvatar())
                .chapter(chapter!=null ? "Chapter " + chapter.getChapterNumber() : null)
                .chapterSlug(chapter!=null ? chapter.getSlug() : null)
                .content(comment.getContent())
                .isMe(false)
                .build();
    }
}
