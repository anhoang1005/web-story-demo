package com.example.webstorydemo.model.story;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChapterRequest {
    private Long storyId;
    private Integer chapterNumber;
    private String chapterTitle;
    private String content;
}
