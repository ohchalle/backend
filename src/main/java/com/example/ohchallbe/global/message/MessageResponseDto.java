package com.example.ohchallbe.global.message;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponseDto {
    private String msg;

    public MessageResponseDto(String msg) {
        this.msg =msg;
    }
}
