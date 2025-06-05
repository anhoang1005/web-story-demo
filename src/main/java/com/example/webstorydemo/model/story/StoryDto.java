package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryDto {

    private Long id;
    private String slug;
    private String title;
    @JsonProperty("cover_url")
    private String coverUrl;
    private String author;
    private String description;
    private Story.Status status;
    private Double rating;
    @JsonProperty("review_count")
    private Long reviewCount;
    @JsonProperty("view_count")
    private Long viewCount;
    @JsonProperty("follow_count")
    private Long followCount;
    @JsonProperty("chapter_update")
    private Integer chapterUpdate;
    @JsonProperty("category_list")
    private List<CategoryDto> categoryList;
    private List<ChapterDto> chapterList;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("chapter_updated_at")
    private String chapterUpdatedAt;

    @JsonProperty("is_follow")
    private Boolean isFollow;
    @JsonProperty("history_chapter")
    private Integer historyChapter;
}
