package com.example.webstorydemo.model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDto {
    @JsonProperty("history_id")
    private Long historyId;
    @JsonProperty("cover_url")
    private String coverUrl;
    @JsonProperty("story_slug")
    private String storySlug;
    @JsonProperty("story_title")
    private String storyTitle;
    private Integer chapter;
    @JsonProperty("updated_at")
    private String updatedAt;
}
