package com.example.hackerNews.hackerNews.service.impl;

import com.example.hackerNews.hackerNews.dto.CommentDto;
import com.example.hackerNews.hackerNews.dto.StoryDto;
import com.example.hackerNews.hackerNews.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final RestTemplate restTemplate;

    private final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    @Override
    public List<StoryDto> getTopStories() {
        int[] storyIds = restTemplate.getForObject(BASE_URL + "topstories.json", int[].class);
        //long fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15).atZone(ZoneId.systemDefault()).toEpochSecond();
        var fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        List<StoryDto> topStories = new ArrayList<>();
        for (int storyId : storyIds) {
            StoryDto topStoryDto = restTemplate.getForObject(BASE_URL + "item/{storyId}.json", StoryDto.class, storyId);
            var time = LocalDateTime.ofInstant(Instant.ofEpochSecond(topStoryDto.getTime()), ZoneId.systemDefault());
            if (time.isAfter(fifteenMinutesAgo)) {
                topStories.add(topStoryDto);
            }
            if (topStories.size() == 10) {
                break;
            }
        }
        topStories.sort(Comparator.comparing(StoryDto::getScore).reversed());
        return topStories;
    }

    @Cacheable(value = "top-stories", key = "'top-stories'")
    public List<StoryDto> getTopStoriesFromCache() {
        // this method will retrieve the cached data
        return null;
    }


    @Override
    public List<CommentDto> getComments(String storyId) {
        StoryDto story = restTemplate.getForObject(BASE_URL + "item/{storyId}.json", StoryDto.class, storyId);
        List<Long> commentIds = story.getKids();
        List<CommentDto> comments = new ArrayList<>();
        for (Long commentId : commentIds) {
            CommentDto comment = restTemplate.getForObject(BASE_URL + "item/{commentId}.json", CommentDto.class, commentId);
            comments.add(comment);
        }
        comments.sort((c1, c2) -> c2.getKids().size() - c1.getKids().size());
        List<CommentDto> commentDtos = new ArrayList<>();
        for (CommentDto comment : comments.subList(0, Math.min(comments.size(), 10))) {
            commentDtos.add(CommentDto.builder()
                            .text(comment.getText())
                            .by(comment.getBy())
                    .build());
        }
        return commentDtos;
    }
}
