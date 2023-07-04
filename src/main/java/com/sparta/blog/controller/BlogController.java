package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
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
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto){
        return blogService.createBlog(requestDto);
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
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto){
        return blogService.updateBlog(id, requestDto);
    }

    @DeleteMapping("/blog/{id}")
    public String deleteBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto){
        return blogService.deleteBlog(id, requestDto);
    }
}