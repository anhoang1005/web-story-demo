package com.example.webstorydemo.controller;

import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.services.CacheService;
import com.example.webstorydemo.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CategoryController {
    private final CacheService cacheService;

    @GetMapping("/api/guest/category")
    public ResponseEntity<?> guestGetAllCategory(){
        ResponseBody<?> responseBody = new ResponseBody<>(cacheService.cacheCategory());
        return ResponseEntity.ok(responseBody);
    }
}
