package com.sparta.blog.controller;

import com.sparta.myblog.dto.MyblogRequestDto;
import com.sparta.myblog.dto.MyblogResponseDto;
import com.sparta.myblog.service.MyblogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyblogController {

    private final MyblogService myblogService;

    public MyblogController(MyblogService myblogService){
        this.myblogService = myblogService;
    }
    @PostMapping("/blog")
    public MyblogResponseDto createMyblog(@RequestBody MyblogRequestDto requestDto){
        return myblogService.createMyblog(requestDto);
    }

    @GetMapping("/blog")
    public List<MyblogResponseDto> getMyblog(){
        return myblogService.getMyblog();
    }

    //조회기능 추가
    @GetMapping("/blog/{id}")
    public MyblogResponseDto getMyblog(@PathVariable Long id, @RequestBody MyblogRequestDto requestDto){
        return myblogService.getMyblog(id, requestDto);
    }

    @PutMapping("/blog/{id}")
    public Long updateMyblog(@PathVariable Long id, @RequestBody MyblogRequestDto requestDto){
        return myblogService.updateMyblog(id, requestDto);
    }

    @DeleteMapping("/blog/{id}")
    public Long deleteMyblog(@PathVariable Long id, @RequestBody MyblogRequestDto requestDto){
        return myblogService.deleteMyblog(id, requestDto);
    }
}