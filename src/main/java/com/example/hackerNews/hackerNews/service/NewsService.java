package com.example.hackerNews.hackerNews.service;

import com.example.hackerNews.hackerNews.dto.CommentDto;
import com.example.hackerNews.hackerNews.dto.StoryDto;

import java.util.List;

public interface NewsService {

    List<StoryDto> getTopStories();

    List<StoryDto> getTopStoriesFromCache();

    List<CommentDto> getComments(String storyId);

}
