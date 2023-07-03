package com.sparta.blog.service;

import com.sparta.myblog.dto.MyblogRequestDto;
import com.sparta.myblog.dto.MyblogResponseDto;
import com.sparta.myblog.entity.Myblog;
import com.sparta.myblog.repository.MyblogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyblogService {
    private final MyblogRepository myblogRepository;

    public MyblogService(MyblogRepository myblogRepository){
        this.myblogRepository = myblogRepository;
    }

    public MyblogResponseDto createMyblog(MyblogRequestDto requestDto) {
        //RequestDto -> Entity
        Myblog myblog = new Myblog(requestDto);

        //DB 저장
        Myblog saveMyblog = myblogRepository.save(myblog);

        //Entity -> ResponseDto
        MyblogResponseDto myblogResponseDto = new MyblogResponseDto(myblog);

        return myblogResponseDto;
    }

    public List<MyblogResponseDto> getMyblog() {
        //DB 조회
        return myblogRepository.findAllByOrderByModifiedAtDesc().stream().map(MyblogResponseDto::new).toList();
    }

    @Transactional
    public Long updateMyblog(Long id, MyblogRequestDto requestDto) {
        //해당 글이 DB에 존재하는지 확인
        Myblog myblog = findMyblog(id, requestDto);

        //글 내용 수정
        myblog.update(requestDto);

        return id;
    }

    public Long deleteMyblog(Long id, MyblogRequestDto requestDto) {
        //해당 글이 DB에 존재하는지 확인
        Myblog myblog = findMyblog(id, requestDto);

        //해당 글 삭제하기
        myblogRepository.delete(myblog);

        return id;
    }

    //글 조회기능 추가
    public MyblogResponseDto getMyblog(Long id, MyblogRequestDto requestDto) {
        //해당 글이 DB에 존재하는지 확인
        Myblog myblog = findMyblog(id, requestDto);

        MyblogResponseDto myblogResponseDto = new MyblogResponseDto(myblog);

        return myblogResponseDto;
    }

    private Myblog findMyblog(Long id, MyblogRequestDto requestDto){
        Myblog myblog = myblogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 글은 존재하지 않습니다."));

        //비밀번호 확인
        if(!myblog.getPassword().equals(requestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return myblog;
    }
}