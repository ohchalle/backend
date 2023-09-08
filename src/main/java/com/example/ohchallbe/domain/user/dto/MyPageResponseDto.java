package com.example.ohchallbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDto {

    private String nickname;
    private String userName;
    private String userEmail;
//    private String userPhoneNumber;


}
