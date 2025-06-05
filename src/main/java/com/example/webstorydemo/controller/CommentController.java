package com.example.webstorydemo.controller;

import com.example.webstorydemo.model.comment.CommentRequest;
import com.example.webstorydemo.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/guest/comment")
    public ResponseEntity<?> guestGetComment(
            @RequestParam(value = "story_slug") String storySlug,
            @RequestParam(value = "chapter_slug", required = false) String chapterSlug,
            @RequestParam(value = "parent_id", required = false) Long parentId,
            @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {

        return ResponseEntity.ok(commentService.guestGetComment(storySlug, chapterSlug, parentId, pageNumber, pageSize));
    }


    @PostMapping("/api/user/comment")
    public ResponseEntity<?> userCreateComment(@RequestBody CommentRequest request){
        return ResponseEntity.ok(commentService.userCreateComment(request));
    }

    @PutMapping("/api/user/comment")
    public ResponseEntity<?> userUpdateComment(@RequestParam("comment_id") Long commentId,
                                               @RequestParam("content") String content){
        return ResponseEntity.ok(commentService.userUpdateComment(commentId, content));
    }

    @DeleteMapping("/api/user/comment")
    public ResponseEntity<?> userUpdateComment(@RequestParam("comment_id") Long commentId){
        return ResponseEntity.ok(commentService.userDeleteComment(commentId));
    }
}
