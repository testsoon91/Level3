package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.StatusCodeDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final JwtUtil jwtUtil;

    public BlogService(BlogRepository blogRepository, JwtUtil jwtUtil){
        this.blogRepository = blogRepository;
        this.jwtUtil = jwtUtil;
    }

    public BlogResponseDto createBlog(BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        //token에서 loginname 가져오기
        String loginname = loginUsername(tokenValue);

        //RequestDto -> Entity
        Blog blog = new Blog(requestDto, loginname);

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
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        //token에서 loginname 가져오기
        String loginname = loginUsername(tokenValue);

        // username 비교
        if (!loginname.equals(blog.getUsername())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
        //글 내용 수정
        blog.update(requestDto);

        return new BlogResponseDto(blog);
    }

    public StatusCodeDto deleteBlog(Long id, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        //token에서 loginname 가져오기
        String loginname = loginUsername(tokenValue);

        // username 비교
        if (!loginname.equals(blog.getUsername())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

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

    private String loginUsername(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token Error");
        }
        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 username
        String loginname = info.getSubject();

        return loginname;
    }
}