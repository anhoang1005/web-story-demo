package com.example.webstorydemo.model.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
    private Long parentId;
    private String story;
    private String chapter;
    private String content;
}
