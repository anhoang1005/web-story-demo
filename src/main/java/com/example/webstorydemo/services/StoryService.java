package com.example.webstorydemo.services;

import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.story.CreateStoryRequest;
import com.example.webstorydemo.model.story.StoryFilter;
import org.springframework.web.multipart.MultipartFile;

public interface StoryService {

    ResponseBody<?> guestGetStoryDetail(String storySlug);
    ResponseBody<?> guestGetAllStory(String title, Story.Status status,
                                     String cateSlug, StoryFilter.SortFilter sortFilter,
                                     int pageNumber, int pageSize);

    ResponseBody<?> userFollowStory(String slug, boolean isFollow);
    ResponseBody<?> userGetFollowStory(int pageNumber, int pageSize);

    ResponseBody<?> adminAddStory(CreateStoryRequest request, MultipartFile file);
    ResponseBody<?> adminUpdateStory(CreateStoryRequest request, MultipartFile file);
    ResponseBody<?> adminDeleteStory(Long storyId, Boolean enable);
}
