package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.StatusCodeDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }
    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue){
        return blogService.createBlog(requestDto, tokenValue);
    }

    @GetMapping("/blog")
    public List<BlogResponseDto> getBlogs(){
        return blogService.getBlogs();
    }

    //조회기능 추가
    @GetMapping("/blog/{id}")
    public BlogResponseDto getBlog(@PathVariable Long id){
        return blogService.getBlog(id);
    }

    @PutMapping("/blog/{id}")
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue){
        return blogService.updateBlog(id, requestDto, tokenValue);
    }

    @DeleteMapping("/blog/{id}")
    public StatusCodeDto deleteMyblog(@PathVariable Long id, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue){
        return blogService.deleteBlog(id, tokenValue);
    }
}