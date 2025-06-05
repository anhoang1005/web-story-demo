package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;

public interface TopStoryViewDTO {
    Story getStory();
    Long getTotalView();
}
