package com.example.webstorydemo.controller;

import com.example.webstorydemo.services.FakerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FakerController {
    private final FakerService fakerService;

    @GetMapping("/api/guest/fake/users")
    public ResponseEntity<?> fakeUser(@RequestParam("user_count") Integer userCount){
        fakerService.generateAdminUsers();
        fakerService.generateCustomUsers(userCount);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/api/guest/fake/story")
    public ResponseEntity<?> fakeStory(@RequestParam("story_count") Integer storyCount){
        fakerService.generateCategory();
        fakerService.generateStory(storyCount);
        fakerService.generateComment();
        fakerService.generateHistory();
        return ResponseEntity.ok("done");
    }

    @GetMapping("/api/guest/fake/view")
    public ResponseEntity<?> fakeView(){
        fakerService.fakeTopView();
        fakerService.fakeStoryFollow();
        return ResponseEntity.ok("done");
    }
}
