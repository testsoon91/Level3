package com.sparta.blog.dto;

import lombok.Data;

@Data
public class StatusCodeDto {
    private String msg;
    private int status;

    public StatusCodeDto(String msg, int status){
        this.msg = msg;
        this.status = status;
    }
}
