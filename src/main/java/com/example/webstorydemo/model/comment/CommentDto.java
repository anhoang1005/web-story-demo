package com.example.webstorydemo.model.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    @JsonProperty("comment_id")
    private Long commentId;
    @JsonProperty("parent_id")
    private Long parentId;
    private String username;
    private String avatar;
    private String chapter;
    @JsonProperty("chapter_slug")
    private String chapterSlug;
    private String content;
    @JsonProperty("is_me")
    private boolean isMe;
}
