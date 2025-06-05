package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryFilter {
    private String title;
    private Story.Status status;
    private Long categoryId;
    private SortFilter sortFilter;
    private TimeFilter timeFilter;
    private int pageNumber;
    private int pageSize;

    public enum TimeFilter{
        ALL,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    @Getter
    public enum SortFilter {
        created_asc("createdAt", "asc"),
        created_desc("createdAt", "desc"),
        view_asc("viewCount", "asc"),
        view_desc("viewCount", "desc"),
        follow_asc("followCount", "asc"),
        follow_desc("followCount", "desc"),
        chapter_asc("chapterUpdate", "asc"),
        chapter_desc("chapterUpdate", "desc");

        private final String value;
        private final String type;
        SortFilter(String value, String type) {
            this.value = value;
            this.type = type;
        }
        public String getValue() {
            return value;
        }
    }

}
