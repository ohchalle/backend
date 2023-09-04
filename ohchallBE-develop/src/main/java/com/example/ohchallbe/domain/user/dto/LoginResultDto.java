package com.example.ohchallbe.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResultDto {
    private String nickname;
    private Boolean isSucces;

}
