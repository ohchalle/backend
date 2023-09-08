package com.example.ohchallbe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    @Schema(description = "엑세스토큰")
    private String accessToken;
    @Schema(description = "리프레시")
    private String refreshToken;

    private String errorMsg;

    private String nickname;





}
