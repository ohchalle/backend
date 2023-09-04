package com.example.ohchallbe.domain.postScrap.controller;

import com.example.ohchallbe.domain.postScrap.dto.PostScrapResponseDto;
import com.example.ohchallbe.domain.postScrap.dto.ScrappedCrewRecruitmentDto;
import com.example.ohchallbe.domain.postScrap.service.PostScrapService;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crew")
public class PostScrapController {

    private final PostScrapService postScrapService;

    @Operation(summary = "크루모집 게시글 스크랩(Scrap) 기능", description = "크루모집 게시글 스크랩(Scrap) 기능 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/{crewRecruitmentId}/scrap")
    public ResponseEntity<PostScrapResponseDto> postScrap(@PathVariable("crewRecruitmentId") Long crewRecruitmentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        boolean isScraped = postScrapService.toggleScrap(crewRecruitmentId, user);
        PostScrapResponseDto response = new PostScrapResponseDto(isScraped);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "크루모집 게시글 스크랩(Scrap) 조회 기능", description = "크루모집 게시글 스크랩(Scrap) 조회 기능 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/scrap")
    public ResponseEntity<List<ScrappedCrewRecruitmentDto>> getScrappedCrewRecruitments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<ScrappedCrewRecruitmentDto> scrappedDtos = postScrapService.getScrappedCrewRecruitments(user);
        return ResponseEntity.ok(scrappedDtos);
    }
}
