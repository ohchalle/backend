package com.example.ohchallbe.domain.comment.controller;

import com.example.ohchallbe.domain.comment.dto.CommentRequestDto;
import com.example.ohchallbe.domain.comment.dto.CommentResponseDto;
import com.example.ohchallbe.domain.comment.service.CommentService;
import com.example.ohchallbe.domain.user.entity.User;
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

import java.util.List;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 조회
    @Operation(summary = "댓글 조회", description = "댓글 조회 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/{crewRecruitmentId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable("crewRecruitmentId") Long crewRecruitmentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String currentUserEmail = userDetails.getUser().getEmail();
        return commentService.getComments(crewRecruitmentId, currentUserEmail);
    }

    //댓글 생성
    @Operation(summary = "댓글 생성", description = "댓글 생성 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/{crewRecruitmentId}/comments")
    public ResponseEntity createComment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
                                                       @RequestBody CommentRequestDto commentRequestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {


        User user = userDetails.getUser();
        commentService.createComment(crewRecruitmentId, commentRequestDto, user);
        return new ResponseEntity("성공", HttpStatus.OK);

    }

    //대댓글 생성
    @Operation(summary = "대댓글 생성", description = "대댓글 생성 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/{crewRecruitmentId}/comments/{parentCommentId}")
    public ResponseEntity createChildComment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
                                             @PathVariable("parentCommentId") Long parentCommentId,
                                             @RequestBody CommentRequestDto commentRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        commentService.createChildComment(crewRecruitmentId, parentCommentId, commentRequestDto, user);

        return new ResponseEntity("성공", HttpStatus.OK);
    }

    // 대댓글 수정
    @Operation(summary = "대댓글 수정", description = "대댓글 수정 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/{crewRecruitmentId}/comments/{parentCommentId}/{commentId}")
    public ResponseEntity updateChildComment(@PathVariable("parentCommentId") Long parentCommentId,
                                             @PathVariable("commentId") Long commentId,
                                             @RequestBody CommentRequestDto commentRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        commentService.updateComment(commentId, commentRequestDto, user);

        return new ResponseEntity("성공", HttpStatus.OK);
    }

    // 대댓글 삭제
    @Operation(summary = "대댓글 삭제", description = "대댓글 삭제 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @DeleteMapping("/{crewRecruitmentId}/comments/{parentCommentId}/{commentId}")
    public ResponseEntity deleteChildComment(@PathVariable("parentCommentId") Long parentCommentId,
                                             @PathVariable("commentId") Long commentId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        commentService.deleteComment(commentId, user);

        return new ResponseEntity("성공", HttpStatus.OK);
    }

//    //대댓글 생성
//    @PostMapping("/{crewRecruitmentId}/comments/{parentCommentId}")
//    public ResponseEntity createChildComment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
//                                             @PathVariable("parentCommentId") Long parentCommentId,
//                                             @RequestBody CommentRequestDto commentRequestDto,
//                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        User user = userDetails.getUser();
//        commentService.createChildComment(crewRecruitmentId, parentCommentId, commentRequestDto, user);
//
//        return new ResponseEntity("성공", HttpStatus.OK);
//    }

    //댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 수정 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/{crewRecruitmentId}/comments/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        commentService.updateComment(commentId, commentRequestDto, user);

        return new ResponseEntity("성공", HttpStatus.OK);

    }

    //댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @DeleteMapping("/{crewRecruitmentId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        commentService.deleteComment(commentId, user);

        return new ResponseEntity("성공", HttpStatus.OK);
    }

}
