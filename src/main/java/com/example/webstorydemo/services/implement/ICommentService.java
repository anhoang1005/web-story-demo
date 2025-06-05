package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.StoryChapter;
import com.example.webstorydemo.entity.StoryComment;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.mapper.CommentMapper;
import com.example.webstorydemo.model.comment.CommentDto;
import com.example.webstorydemo.model.comment.CommentRequest;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.repository.CommentRepository;
import com.example.webstorydemo.repository.StoryChapterRepository;
import com.example.webstorydemo.repository.StoryRepository;
import com.example.webstorydemo.repository.UserRepository;
import com.example.webstorydemo.services.CommentService;
import com.example.webstorydemo.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ICommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final StoryChapterRepository chapterRepository;

    private final CommentMapper commentMapper;
    private final AuthUtils authUtils;


    @Override
    @Transactional
    public ResponseBody<?> guestGetComment(String storySlug, String chapterSlug, Long parentId,
                                           int pageNumber, int pageSize) {
        try{
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize,
                    Sort.by(Sort.Order.desc("createdAt")));
            Page<StoryComment> page = commentRepository
                    .guestGetPageComment(storySlug, chapterSlug, parentId, pageable);
            List<CommentDto> dtoList = page.stream()
                    .map(commentMapper::entityToDto).collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(dtoList).pageNumber(pageNumber).pageSize(pageSize)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("get comment error! " + e.getMessage());
            throw new RequestNotFoundException("get comment error! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userCreateComment(CommentRequest request) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users =userRepository.findUsersById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("user not found"));
            Story story = storyRepository.findStoryBySlug(request.getStory())
                    .orElseThrow(() -> new RequestNotFoundException("story not found"));
            StoryComment comment = new StoryComment();
            comment.setStory(story);
            comment.setUsers(users);
            if(request.getChapter()!=null){
                StoryChapter chapter = chapterRepository
                        .guestGetChapterByStorySlugAndChapterSlug(request.getStory(),request.getChapter())
                        .orElseThrow(() -> new RequestNotFoundException("chapter not found"));
                comment.setChapter(chapter);
            }
            if(request.getParentId()!=null){
                StoryComment parent = commentRepository.findById(request.getParentId())
                        .orElseThrow(() -> new RequestNotFoundException("Parent comment not found!"));
                comment.setParentComment(parent);
                comment.setLevel(parent.getLevel() + 1);
            } else{
                comment.setLevel(0);
            }
            comment.setContent(request.getContent());
            comment = commentRepository.save(comment);
            return new ResponseBody<>(commentMapper.entityToDto(comment));
        } catch (Exception e){
            log.error("get comment error! " + e.getMessage());
            throw new RequestNotFoundException("get comment error! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userUpdateComment(Long commentId, String content) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            StoryComment comment = commentRepository.checkExistedComment(userId, commentId)
                    .orElseThrow(() -> new RequestNotFoundException("comment not found"));
            comment.setContent(content);
            comment = commentRepository.save(comment);
            return new ResponseBody<>(commentMapper.entityToDto(comment));
        } catch (Exception e){
            log.error("get comment error! " + e.getMessage());
            throw new RequestNotFoundException("get comment error! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userDeleteComment(Long commentId) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            StoryComment comment = commentRepository.checkExistedComment(userId, commentId)
                    .orElseThrow(() -> new RequestNotFoundException("comment not found"));
            commentRepository.delete(comment);
            return new ResponseBody<>("OK");
        } catch (Exception e){
            log.error("get comment error! " + e.getMessage());
            throw new RequestNotFoundException("get comment error! " + e.getMessage());
        }
    }
}
