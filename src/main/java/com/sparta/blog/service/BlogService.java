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

    //게시글 작성
    public BlogResponseDto createBlog(BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        String loginname = loginUsername(tokenValue);

            //RequestDto -> Entity
            Blog blog = new Blog(requestDto, loginname);

            //DB 저장
            Blog saveBlog = blogRepository.save(blog);

            //Entity -> ResponseDto
            BlogResponseDto blogResponseDto = new BlogResponseDto(blog);

            return blogResponseDto;
        }

        //전체 게시글 조회
    public List<BlogResponseDto> getBlog() {
        //DB 조회
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    //게시글 수정
    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        String loginname = loginUsername(tokenValue);
        if(!loginname.equals(blog.getUsername())){
            throw new IllegalArgumentException("작성자만 수정가능합니다.");
        }
        //글 내용 수정
        blog.update(requestDto);

        return new BlogResponseDto(blog);
    }

    //게시글 삭제하기
    public StatusCodeDto deleteBlog(Long id, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        //해당 글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        String loginname = loginUsername(tokenValue);
        if(!loginname.equals(blog.getUsername())){
            throw new IllegalArgumentException("작성자만 삭제가능합니다.");
        }

        blogRepository.delete(blog);

        return new StatusCodeDto("삭제 성공", HttpStatus.OK.value());
    }

    //선택한 게시글 조회
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

    //유효한 토큰인지 검사
    private String loginUsername(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String loginname = info.getSubject();
        return loginname;
    }
}