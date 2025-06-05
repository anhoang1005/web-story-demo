package com.example.webstorydemo.controller;

import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.story.CreateStoryRequest;
import com.example.webstorydemo.model.story.StoryFilter;
import com.example.webstorydemo.model.story.StoryRankDto;
import com.example.webstorydemo.services.CacheService;
import com.example.webstorydemo.services.ChapterService;
import com.example.webstorydemo.services.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class StoryController {
    private final StoryService storyService;
    private final CacheService cacheService;
    private final ChapterService chapterService;

    @GetMapping("/api/guest/story/{story_slug}")
    public ResponseEntity<?> guestGetStory(@PathVariable("story_slug") String storySlug){
        return ResponseEntity.ok(storyService.guestGetStoryDetail(storySlug));
    }

    @GetMapping("/api/guest/story/chapter-list")
    public ResponseEntity<?> guestGetPageChapter(@RequestParam("story_slug") String storySlug,
                                                 @RequestParam(name = "is_asc", defaultValue = "true") boolean sort,
                                                 @RequestParam(name = "page_number", defaultValue = "1") int pageNumber,
                                                 @RequestParam(name = "page_size", defaultValue = "20") int pageSize){
        return ResponseEntity.ok(chapterService.guestGetPageChapter(storySlug, sort, pageNumber, pageSize));
    }

    @GetMapping("/api/guest/story/{story_slug}/{chapter_slug}")
    public ResponseEntity<?> guestGetChapterContent(@PathVariable("story_slug") String storySlug,
                                                    @PathVariable("chapter_slug") String chapterSlug){
        return ResponseEntity.ok(chapterService.guestGetChapterContent(storySlug, chapterSlug));
    }

    @GetMapping("/api/guest/story-filter")
    public ResponseEntity<?> guestGetFilterStory(@RequestParam(name = "title", required = false) String title,
                                                 @RequestParam(name = "category", required = false) String categorySlug,
                                                 @RequestParam(name = "status", required = false) Story.Status status,
                                                 @RequestParam(name = "sort", required = false) StoryFilter.SortFilter sortFilter,
                                                 @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                                 @RequestParam(name = "size", defaultValue = "20") int pageSize){
        return ResponseEntity.ok(storyService.guestGetAllStory(title, status, categorySlug,
                sortFilter, pageNumber, pageSize));
    }

    @GetMapping("/api/guest/story/rank-filter")
    public ResponseEntity<?> guestGetRankFilterStory(@RequestParam("type") StoryRankDto.TopRank type,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size){
        List<StoryRankDto> dtoList;
        if(type== StoryRankDto.TopRank.VIEW_ONE_DAY){
            dtoList = cacheService.cacheStoryRankInToday(page, size);
        } else if(type== StoryRankDto.TopRank.VIEW_SEVEN_DAY){
            dtoList = cacheService.cacheStoryRankInSevenDay(page, size);
        } else{
            dtoList = cacheService.cacheStoryRankInThirtyDay(page, size);
        }
        PageData<?> pageData = PageData.builder()
                .data(dtoList)
                .pageSize(size)
                .pageNumber(page)
                .totalPage(null)
                .totalData(null)
                .build();
        return ResponseEntity.ok(new ResponseBody<>(pageData));
    }

    @GetMapping("/api/user/story/follow")
    public ResponseEntity<?> userGetPageFollow(@RequestParam("page_size") int pageSize,
                                               @RequestParam("page_number") int pageNumber){
        return ResponseEntity.ok(storyService.userGetFollowStory(pageNumber, pageSize));
    }

    @PostMapping("/api/user/story/follow")
    public ResponseEntity<?> userFollowStory(@RequestParam("story") String storySlug,
                                             @RequestParam("is_follow") Boolean isFollow){
        return ResponseEntity.ok(storyService.userFollowStory(storySlug, isFollow));
    }

    @PostMapping("/api/admin/story")
    public ResponseEntity<?> adminAddStory(@RequestPart("data") CreateStoryRequest request,
                                           @RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(storyService.adminAddStory(request, file));
    }
}
