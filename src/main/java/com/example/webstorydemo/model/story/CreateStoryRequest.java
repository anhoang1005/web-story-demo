package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStoryRequest {
    private Long id;
    private String title;
    private String slug;
    private String author;
    private String description;
    private Story.Status status;
    private List<Long> categoryList;
    private List<CreateChapterRequest> chapterList;
}
