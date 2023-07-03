package com.sparta.blog.dto;

import com.sparta.myblog.entity.Myblog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyblogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MyblogResponseDto(Myblog myblog) {
        this.id = myblog.getId();
        this.title = myblog.getTitle();
        this.username = myblog.getUsername();
        this.contents = myblog.getContents();
        this.createdAt = myblog.getCreatedAt();
        this.modifiedAt = myblog.getModifiedAt();
    }
}