package com.example.webstorydemo.controller;

import com.example.webstorydemo.services.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/api/user/history")
    public ResponseEntity<?> userGetHistory(@RequestParam("page_number") int pageNumber,
                                            @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(historyService.userGetPageHistory(pageNumber, pageSize));
    }

    @DeleteMapping("/api/user/history/{history_id}")
    public ResponseEntity<?> userGetHistory(@PathVariable("history_id") Long historyId){
        return ResponseEntity.ok(historyService.userDeleteHistory(historyId));
    }

}
