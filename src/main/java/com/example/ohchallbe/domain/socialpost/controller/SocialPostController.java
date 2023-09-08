package com.example.ohchallbe.domain.socialpost.controller;

import com.example.ohchallbe.domain.crewRecruitment.dto.CrewApplyResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentRequestDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.JoinCrewResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.JoinCrewRepository;
import com.example.ohchallbe.domain.crewRecruitment.service.CrewRecruitmentService;
import com.example.ohchallbe.domain.crewRecruitment.service.JoinCrewService;
import com.example.ohchallbe.domain.socialpost.dto.SocialPostRequestDto;
import com.example.ohchallbe.domain.socialpost.dto.SocialPostResponseDto;
import com.example.ohchallbe.domain.socialpost.service.SocialPostService;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.global.message.MessageResponseDto;
import com.example.ohchallbe.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/socialPost")
@RequiredArgsConstructor
public class SocialPostController {
    private final SocialPostService socialPostService;

    @Operation(summary = "SNS 게시글 작성", description = "SNS 게시글 작성 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("")
    public ResponseEntity<SocialPostResponseDto> createSocialPost(
            @RequestPart(value = "data",required = true) SocialPostRequestDto socialPostRequestDto,
            @RequestPart(value = "image",required = false) List<MultipartFile> images,
            @AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException {
        User user = userDetails.getUser();
        SocialPostResponseDto socialPostResponseDto
                = new SocialPostResponseDto(socialPostService.createSocialPost(socialPostRequestDto, images, user));
        return ResponseEntity.ok(socialPostResponseDto);
    }

    @Operation(summary = "SNS 게시글 상세페이지", description = "SNS 게시글 상세페이지 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/{socialPostId}")
    public SocialPostResponseDto getSocialPost(@PathVariable("socialPostId") Long socialPostId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        socialPostService.updateView(socialPostId);
        return socialPostService.getSocialPost(socialPostId,user);
    }



    @Operation(summary = "SNS 게시글 페이지네이션", description = "SNS 게시글 페이지네이션 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/thumbnailSocialPost")
    public ResponseEntity<Map<String, Object>> getWholeSocialPost(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("sortBy") String sortBy,
                                                                  @RequestParam("isAsc") boolean isAsc) {
        Map<String, Object> socialPostResponseDtoList = socialPostService.getWholeSocialPost(
                page -1,
                size,
                sortBy,
                isAsc
        );
        return ResponseEntity.ok(socialPostResponseDtoList);
    }



    @Operation(summary = "SNS 게시글 삭제", description = "SNS 게시글 삭제 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @DeleteMapping("/{socialPostId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteSocialPost(@PathVariable("socialPostId") Long socialPostId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        socialPostService.deleteSocialPost(socialPostId, user);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }


}
