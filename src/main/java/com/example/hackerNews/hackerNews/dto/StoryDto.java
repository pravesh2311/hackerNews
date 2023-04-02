package com.example.hackerNews.hackerNews.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoryDto {
    private Long id;
    private String title;
    private String url;
    private Integer score;
    private Long time;
    private String by;
    private List<Long> kids;
}