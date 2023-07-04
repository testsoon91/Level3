package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.StatusCodeDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.repository.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository){
        this.blogRepository = blogRepository;
    }

    public BlogResponseDto createBlog(BlogRequestDto requestDto) {
        //RequestDto -> Entity
        Blog blog = new Blog(requestDto);

        //DB 저장
        Blog saveBlog = blogRepository.save(blog);

        //Entity -> ResponseDto
        BlogResponseDto blogResponseDto = new BlogResponseDto(blog);

        return blogResponseDto;
    }

    public List<BlogResponseDto> getBlogs() {
        //DB 조회
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        //비밀번호 확인
        blog = passCheck(blog, requestDto);

        //글 내용 수정
        blog.update(requestDto);

        return new BlogResponseDto(blog);
    }

    public StatusCodeDto deleteBlog(Long id, BlogRequestDto requestDto) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        //비밀번호 확인
        blog = passCheck(blog, requestDto);

        //해당 글 삭제하기
        blogRepository.delete(blog);

        return new StatusCodeDto(HttpStatus.OK.value(),"게시글 삭제 성공");
    }

    //글 조회기능 추가
    public BlogResponseDto getBlog(Long id) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        BlogResponseDto blogResponseDto = new BlogResponseDto(blog);

        return blogResponseDto;
    }

    private Blog findBlog(Long id){
        Blog blog = blogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 글은 존재하지 않습니다."));

        return blog;
    }

    private Blog passCheck(Blog blog, BlogRequestDto requestDto){
        //비밀번호 확인
        if(blog.getPassword() != null && !blog.getPassword().equals(requestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return blog;
    }
}