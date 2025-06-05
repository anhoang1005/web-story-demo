package com.example.webstorydemo.model.story;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChapterDto {
    private Long id;
    @JsonProperty("chapter_number")
    private Integer chapterNumber;
    @JsonProperty("max_chapter")
    private Integer maxChapter;
    @JsonProperty("story_title")
    private String storyTitle;
    private String slug;
    private String title;
    private String content;
    @JsonProperty("created_at")
    private String createdAt;
}
