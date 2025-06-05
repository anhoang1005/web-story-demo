package com.example.webstorydemo.services;

import com.example.webstorydemo.model.comment.CommentRequest;
import com.example.webstorydemo.model.payload.ResponseBody;

public interface CommentService {

    ResponseBody<?> guestGetComment(String storySlug, String chapterSlug, Long parentId,
                                    int pageNumber, int pageSize);

    ResponseBody<?> userCreateComment(CommentRequest request);
    ResponseBody<?> userUpdateComment(Long commentId, String content);
    ResponseBody<?> userDeleteComment(Long commentId);

}
