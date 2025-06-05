package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.config.security.user_detail.CustomUserDetail;
import com.example.webstorydemo.entity.*;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.mapper.StoryMapper;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.story.*;
import com.example.webstorydemo.repository.*;
import com.example.webstorydemo.services.FileService;
import com.example.webstorydemo.services.HistoryService;
import com.example.webstorydemo.services.StoryService;
import com.example.webstorydemo.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IStoryService implements StoryService {
    private final StoryMapper storyMapper;
    private final StoryRepository storyRepository;
    private final CategoryRepository categoryRepository;
    private final UserFollowRepository followRepository;
    private final UserRepository userRepository;

    private final AuthUtils authUtils;
    private final HistoryService historyService;
    private final FileService fileService;

    @Override
    @Transactional
    public ResponseBody<?> guestGetStoryDetail(String storySlug) {
        try{
            Story story = storyRepository.findStoryBySlug(storySlug)
                    .orElseThrow(() -> new RequestNotFoundException("story not found!"));
            StoryDto dto = storyMapper.entityToStoryDto(story);

            CustomUserDetail userDetail = authUtils.getUserFromAuthentication();
            if(userDetail!=null){
                UserFollow follow = followRepository.findByStory_SlugAndUsers_Id(storySlug, userDetail.getId())
                        .orElse(null);
                ReadingHistory history = historyService.findHistoryByStoryAndUser(storySlug, userDetail.getId());
                dto.setIsFollow(follow != null);
                dto.setHistoryChapter(history!=null ? history.getChapter().getChapterNumber() : null);
            }
            return new ResponseBody<>(dto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> guestGetAllStory(String title, Story.Status status,
                                            String cateSlug, StoryFilter.SortFilter sortFilter,
                                            int pageNumber, int pageSize) {
        try{
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            if(sortFilter!=null){
                if(sortFilter.getType().equals("asc")) {
                    sort = Sort.by(Sort.Order.asc(sortFilter.getValue()));
                } else{
                    sort = Sort.by(Sort.Order.desc(sortFilter.getValue()));
                }
            }
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Category category = null;
            if(cateSlug!=null){
                category = categoryRepository.findCategoryBySlug(cateSlug)
                        .orElseThrow(() -> new RequestNotFoundException("category not found!"));
            }
            Page<Story> page = storyRepository.guestGetFilterStory(title, status, category, pageable);
            List<StoryShortDto> dtoList = page.stream().map(storyMapper::entityToStoryShortDto)
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(dtoList)
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .pageSize(page.getSize())
                    .pageNumber(page.getNumber() + 1)
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("Get page story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userFollowStory(String slug, boolean isFollow) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Story story = storyRepository.findStoryBySlug(slug)
                    .orElseThrow(() -> new RequestNotFoundException("story not found!"));
            Users users = userRepository.findUsersById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("user not found!"));
            if(isFollow){
                UserFollow checkFollow = followRepository.findByStory_SlugAndUsers_Id(slug, userId)
                        .orElse(null);
                if(checkFollow!=null){
                    return new ResponseBody<>("OK");
                } else{
                    UserFollow follow = new UserFollow();
                    follow.setStory(story);
                    follow.setUsers(users);
                    followRepository.save(follow);
                    story.setFollowCount(story.getFollowCount() + 1);
                }
            } else {
                UserFollow follow = followRepository.findByStory_SlugAndUsers_Id(slug, userId)
                        .orElseThrow(() -> new RequestNotFoundException("follow not found!"));
                followRepository.delete(follow);
                story.setFollowCount(story.getFollowCount() - 1);
            }
            storyRepository.save(story);
            return new ResponseBody<>("OK");
        } catch (Exception e){
            log.error("Get page story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetFollowStory(int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Page<Story> page = followRepository.userGetByUserId(
                    userId, PageRequest.of(pageNumber - 1, pageSize,
                    Sort.by(Sort.Order.desc("createdAt"))));
            List<StoryShortDto> dtoList = page.stream()
                    .map(storyMapper::entityToStoryShortDto)
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(dtoList)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("Get page story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminAddStory(CreateStoryRequest request, MultipartFile file) {
        try{
            Story checkSlug = storyRepository.findStoryBySlug(request.getSlug())
                    .orElse(null);
            if(checkSlug!=null){
                return new ResponseBody<>("SLUG_EXISTED!");
            }
            Story story = new Story();
            story.setTitle(request.getTitle());
            story.setSlug(request.getSlug());
            story.setAuthor(request.getAuthor());
            story.setDescription(request.getDescription());
            story.setStatus(request.getStatus());
            story.setReviewCount(0L);
            story.setViewCount(0L);
            story.setFollowCount(0L);
            //Category
            List<Category> categoryList = categoryRepository.findByIdIn(request.getCategoryList());
            story.setCategoryList(categoryList);
            //Chapter
            List<StoryChapter> chapterList = new ArrayList<>();
            for(CreateChapterRequest chapterReq : request.getChapterList()){
                StoryChapter chapter = new StoryChapter();
                chapter.setChapterNumber(chapterReq.getChapterNumber());
                chapter.setTitle(chapterReq.getChapterTitle());
                chapter.setSlug("chapter=" + chapterReq.getChapterNumber());
                chapter.setStory(story);
                chapter.setChapterContent(new ChapterContent(chapterReq.getContent(), chapter));
                chapterList.add(chapter);
            }
            story.setChapterList(chapterList);
            story.setChapterUpdate(chapterList.size());

            String fileUrl = fileService.uploadToCloudinary(file);
            if(fileUrl!=null){
                story.setCoverUrl(fileUrl);
            }
            story = storyRepository.save(story);
            return new ResponseBody<>(storyMapper.entityToStoryShortDto(story));
        } catch (Exception e){
            log.error("create story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminUpdateStory(CreateStoryRequest request, MultipartFile file) {
        try {
            Story story = storyRepository.findById(request.getId())
                    .orElseThrow(() -> new RequestNotFoundException("story not found!"));

            if (!story.getSlug().equals(request.getSlug())) {
                Story checkSlug = storyRepository.findStoryBySlug(request.getSlug()).orElse(null);
                if (checkSlug != null) {
                    return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                            "SLUG_EXISTED", ResponseBody.Code.SUCCESS);
                }
                story.setSlug(request.getSlug());
            }
            story.setTitle(request.getTitle());
            story.setAuthor(request.getAuthor());
            story.setDescription(request.getDescription());
            story.setStatus(request.getStatus());

            // Cập nhật category nếu có
            if (request.getCategoryList() != null && !request.getCategoryList().isEmpty()) {
                List<Category> categoryList = categoryRepository.findByIdIn(request.getCategoryList());
                story.setCategoryList(categoryList);
            }

            // Cập nhật chapter nếu có
            if (request.getChapterList() != null && !request.getChapterList().isEmpty()) {
                List<StoryChapter> newChapters = new ArrayList<>();
                for (CreateChapterRequest chapterReq : request.getChapterList()) {
                    StoryChapter chapter = new StoryChapter();
                    chapter.setChapterNumber(chapterReq.getChapterNumber());
                    chapter.setTitle(chapterReq.getChapterTitle());
                    chapter.setSlug("chapter=" + chapterReq.getChapterNumber());
                    chapter.setStory(story);
                    chapter.setChapterContent(new ChapterContent(chapterReq.getContent(), chapter));
                    newChapters.add(chapter);
                }

                // Có thể cần xoá chapter cũ tùy use case (nếu muốn overwrite)
                story.setChapterList(newChapters);
                story.setChapterUpdate(newChapters.size());
            }

            if (file != null && !file.isEmpty()) {
                String fileUrl = fileService.uploadToCloudinary(file);
                if (fileUrl != null) {
                    story.setCoverUrl(fileUrl);
                }
            }

            story = storyRepository.save(story);
            return new ResponseBody<>(storyMapper.entityToStoryShortDto(story));


        } catch (Exception e){
            log.error("create story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminDeleteStory(Long storyId, Boolean enable) {
        try{
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new RequestNotFoundException("story not found!"));
            story.setEnable(enable);
            story = storyRepository.save(story);
            return new ResponseBody<>(storyMapper.entityToStoryShortDto(story));
        } catch (Exception e){
            log.error("delete story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }
}
