package com.example.ohchallbe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
   @Schema(description = "아이디")
   private String useremail;
   @Schema(description = "비밀번호")
   private String password;

}
