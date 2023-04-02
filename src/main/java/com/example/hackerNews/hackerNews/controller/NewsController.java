package com.example.hackerNews.hackerNews.controller;

import com.example.hackerNews.hackerNews.dto.CommentDto;
import com.example.hackerNews.hackerNews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackerNews.hackerNews.dto.StoryDto;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService hackerNewsService;

    @Cacheable(value = "topStoriesCache", key = "'topStories'")
    @GetMapping("/top-stories")
    public List<StoryDto> getTopStories() {
        return hackerNewsService.getTopStories();
    }

    @Cacheable(value = "pastStoriesCache", key = "'pastStories'")
    @GetMapping("/past-stories")
    public List<StoryDto> getPastStories() {
        return hackerNewsService.getTopStoriesFromCache();
    }

    @Cacheable(value = "commentsCache", key = "'comments_'+#storyId")
    @GetMapping("/comments/{storyId}")
    public List<CommentDto> getComments(@PathVariable String storyId) {
        return hackerNewsService.getComments(storyId);
    }
}
