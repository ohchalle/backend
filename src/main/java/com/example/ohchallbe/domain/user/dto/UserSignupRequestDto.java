package com.example.ohchallbe.domain.user.dto;


import lombok.Getter;

@Getter
public class UserSignupRequestDto {

    private String nickname;
    private String userName;
    private String password;
    private String pwCheck;
    private String useremail;
//    private String phonenumber;
    private boolean admin = false;
    private String adminToken= "";
}
