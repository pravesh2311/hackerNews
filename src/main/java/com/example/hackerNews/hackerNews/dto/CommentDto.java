package com.example.hackerNews.hackerNews.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class CommentDto {

    private List<Integer> kids;
    private String text;
    private String by;
}
