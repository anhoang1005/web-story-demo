package com.example.webstorydemo.services;

public interface FakerService {

    void generateCustomUsers(Integer userCount);
    void generateAdminUsers();

    void generateCategory();
    void generateStory(int storyCount);
    void generateComment();
    void generateHistory();

    void fakeTopView();
    void fakeStoryFollow();
}
