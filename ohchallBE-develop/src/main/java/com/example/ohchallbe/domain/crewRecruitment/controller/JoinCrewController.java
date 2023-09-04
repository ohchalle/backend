package com.example.ohchallbe.domain.crewRecruitment.controller;


import com.example.ohchallbe.domain.crewRecruitment.dto.JoinCrewResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.crewRecruitment.service.CrewRecruitmentService;
import com.example.ohchallbe.domain.crewRecruitment.service.JoinCrewService;
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
@RequestMapping("/api/joinCrew")
@RequiredArgsConstructor
public class JoinCrewController {

    private final JoinCrewService joinCrewService;

    @Operation(summary = "크루신청자 조회", description = "크루신청자 조회 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/{isAceppted}")
    public ResponseEntity<List<JoinCrewResponseDto>> getJoinCrew(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Boolean isAceppted){

        String userEmail = userDetails.getUser().getEmail();

        return ResponseEntity.ok(joinCrewService.getJoinCrewList(userEmail,isAceppted));


    }

    @Operation(summary = "크루 신청자 승인", description = "크루 신청자 승인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/{joinCrewId}")
    public ResponseEntity acceptJoinCrewApply(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long joinCrewId ) {
        String userEmail = userDetails.getUser().getEmail();

        try {
            joinCrewService.AcceptJoinCrewApply(userEmail, joinCrewId);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @Operation(summary = "상세페이지 크루신청자 조회", description = "상세페이지 크루신청자 조회 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/{crewRecruitmentId}/{isAceppted}")
    public ResponseEntity<List<JoinCrewResponseDto>> getDetailPageJoinCrew(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long crewRecruitmentId, @PathVariable Boolean isAceppted){



        return ResponseEntity.ok(joinCrewService.getDetailPageJoinCrewList(crewRecruitmentId,isAceppted,userDetails));


    }


    @Operation(summary = "상세 페이지 크루 신청자 승인", description = "상세 페이지 크루 신청자 승인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/detail/{crewRecruitmentId}")
    public ResponseEntity acceptDetailPageJoinCrewApply(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long crewRecruitmentId ,@RequestParam(value = "applyerEmail") String applyerEmail ) {
        String userEmail = userDetails.getUser().getEmail();

        try {

            return ResponseEntity.ok(joinCrewService.acceptDetailPageJoinCrewApply(userEmail, crewRecruitmentId,applyerEmail));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}
