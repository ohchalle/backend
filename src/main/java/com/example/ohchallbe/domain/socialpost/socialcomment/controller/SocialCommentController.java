package com.example.ohchallbe.domain.socialpost.socialcomment.controller;

import com.example.ohchallbe.domain.socialpost.socialcomment.dto.SocialCommentRequestDto;
import com.example.ohchallbe.domain.socialpost.socialcomment.dto.SocialCommentResponseDto;
import com.example.ohchallbe.domain.socialpost.socialcomment.service.SocialCommentService;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.global.message.MessageResponseDto;
import com.example.ohchallbe.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socialPost/comment")
@RequiredArgsConstructor
public class SocialCommentController {
    private final SocialCommentService socialCommentService;

    @Operation(summary = "SNS 댓글 생성입니다", description = "SNS 댓글 생성 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @PostMapping("/{socialPostId}")
    public SocialCommentResponseDto createComment(@PathVariable("socialPostId") Long socialPostId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestBody SocialCommentRequestDto socialCommentRequestDto){
        User user = userDetails.getUser();
        return socialCommentService.createComment(socialPostId, user,socialCommentRequestDto);
    }


    @Operation(summary = "SNS 댓글 조회입니다", description = "SNS 댓글 조회 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @GetMapping("/{socialPostId}")
    public List<SocialCommentResponseDto> getSocialCommentsBySocialPostId(@PathVariable("socialPostId") Long socialPostId){
        return socialCommentService.getSocialCommentsBySocialPostId(socialPostId);
    }


    @Operation(summary = "SNS 댓글 수정입니다", description = "SNS 댓글 수정 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @PutMapping("/{socialCommentId}")
    public SocialCommentResponseDto updateComment(@PathVariable("socialCommentId") Long socialCommentId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody SocialCommentRequestDto socialCommentRequestDto){
        User user = userDetails.getUser();
        return socialCommentService.updateComment(socialCommentId, user, socialCommentRequestDto);
    }


    @Operation(summary = "SNS 댓글 삭제입니다", description = "댓글 삭제 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @DeleteMapping("/{socialCommentId}")
    public Boolean deleteComment(@PathVariable("socialCommentId") Long socialCommentId,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return socialCommentService.deleteComment(socialCommentId, user);
    }
}
