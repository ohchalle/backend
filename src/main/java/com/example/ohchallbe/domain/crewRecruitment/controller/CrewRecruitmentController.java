package com.example.ohchallbe.domain.crewRecruitment.controller;

import com.example.ohchallbe.domain.crewRecruitment.dto.CrewApplyResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentRequestDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.JoinCrewResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.JoinCrewRepository;
import com.example.ohchallbe.domain.crewRecruitment.service.CrewRecruitmentService;
import com.example.ohchallbe.domain.crewRecruitment.service.JoinCrewService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewRecruitmentController {

    private final CrewRecruitmentService crewRecruitmentService;


    @Operation(summary = "크루모집 운동 카테고리 검색", description = "크루모집 운동 카테고리 검색 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/exerciseKind")
    public ResponseEntity<Map<String, Object>> getCrewRecruitmentByExerciseKind(
            @RequestParam("exerciseKind") String exerciseKind,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        Map<String, Object> crewRecruitmentResponseDtoList = crewRecruitmentService.getCrewRecruitmentByExerciseKind(
                exerciseKind,
                page - 1,
                size,
                sortBy,
                isAsc
        );

        return ResponseEntity.ok(crewRecruitmentResponseDtoList);

    }

    @Operation(summary = "페이지네이션", description = "페이지네이션 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getWholeCrewRecruitment(@RequestParam("page") int page,
                                                                       @RequestParam("size") int size,
                                                                       @RequestParam("sortBy") String sortBy,
                                                                       @RequestParam("isAsc") boolean isAsc) {

        Map<String, Object> crewRecruitmentResponseDtoList = crewRecruitmentService.getWholeCrewRecruitment(
                page - 1,
                size,
                sortBy,
                isAsc
        );

        return ResponseEntity.ok(crewRecruitmentResponseDtoList);
    }

    @Operation(summary = "크루모집 게시글 검색(Basic)", description = "크루모집 게시글 검색(Basic) api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/search/basic")
    public ResponseEntity<Map<String, Object>> searchCrewBasic(
            @RequestParam("keyword") String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {

        try {
            String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8.toString());

            Map<String, Object> newsResponseDtoList = crewRecruitmentService.searchCrewBasic(
                    decodedKeyword,
                    page - 1,
                    size,
                    sortBy,
                    isAsc
            );

            return ResponseEntity.ok(newsResponseDtoList);
        } catch (UnsupportedEncodingException e) {
            // 디코딩 예외 처리
            // 에러를 로그로 기록하거나 적절한 조치를 취하거나 에러 응답을 반환할 수 있습니다.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "크루모집 게시글 검색", description = "크루모집 게시글 검색 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchCrew(@RequestParam("keyword") String keyword,
                                                          @RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        try {
            String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8.toString());

            Map<String, Object> crewRecruitmentResponseDtoList = crewRecruitmentService.searchCrew(
                    decodedKeyword, page - 1, size);

            return ResponseEntity.ok(crewRecruitmentResponseDtoList);
        } catch (UnsupportedEncodingException e) {
            // 디코딩 예외 처리
            // 에러를 로그로 기록하거나 적절한 조치를 취하거나 에러 응답을 반환할 수 있습니다.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    //     크루 모집 게시글 작성
    @Operation(summary = "크루모집 게시글 작성", description = "크루모집 게시글 작성 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("")
    public ResponseEntity<CrewRecruitmentResponseDto> createCrewRecruitment(@RequestPart(value = "data", required = true) CrewRecruitmentRequestDto crewRecruitmentRequestDto, @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        boolean isOwner;
        User user = userDetails.getUser();
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentService.createCrewRecruitment(crewRecruitmentRequestDto, user, images);
        if(crewRecruitmentEntity.getUserEmail().equals(userDetails.getUser().getEmail())){
            isOwner = true;
        } else {
            isOwner = false;
        }
        CrewRecruitmentResponseDto crewRecruitmentResponseDto = new CrewRecruitmentResponseDto(crewRecruitmentEntity);

        return ResponseEntity.ok(crewRecruitmentResponseDto);
    }

    //크루 가입 신청

    @Operation(summary = "크루 가입신청", description = "크루 가입신청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/{crewRecruitmentId}")
    public CrewApplyResponseDto createCrewApply(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        CrewApplyResponseDto crewApplyResponseDto;

        try {
            crewApplyResponseDto = crewRecruitmentService.createCrewApply(crewRecruitmentId, user);

        } catch (Exception e) {

            return crewApplyResponseDto = new CrewApplyResponseDto(null, e.getMessage());
        }
//        return new ResponseEntity(crewRecruitmentId,HttpStatus.OK);
        return crewApplyResponseDto;

    }

    //크루 모집 게시글 상세페이지
    @Operation(summary = "크루모집 게시글 상세페이지", description = "크루모집 게시글 상세페이지 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/{crewRecruitmentId}")
    public CrewRecruitmentResponseDto getCrewRecruitment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();


        crewRecruitmentService.updateVew(crewRecruitmentId);
        return crewRecruitmentService.getCrewRecruitment(crewRecruitmentId,user);
    }

    //크루모집 post 삭제

    @Operation(summary = "크루모집 게시글 삭제", description = "크루모집 게시글 삭제 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @DeleteMapping("/{crewRecruitmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteCrewRecruitment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        crewRecruitmentService.deleteCrewRecruitment(crewRecruitmentId, user);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }


    //크루모집 post 수정
    @Operation(summary = "크루모집 게시글 수정", description = "크루모집 게시글 수정 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/{crewRecruitmentId}")
    public ResponseEntity updateCrewRecruitment(@PathVariable("crewRecruitmentId") Long crewRecruitmentId,
                                                @RequestBody CrewRecruitmentRequestDto crewRecruitmentRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        crewRecruitmentService.updateCrewRecruitment(crewRecruitmentId, crewRecruitmentRequestDto, user);

        return new ResponseEntity("성공", HttpStatus.OK);
    }

    @Operation(summary = "페이지네이션", description = "페이지네이션 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/more")
    public ResponseEntity<Map<String, Object>> getMypageCrewRecruitment(@RequestParam("page") int page,
                                                                        @RequestParam("size") int size,
                                                                        @RequestParam("sortBy") String sortBy,
                                                                        @RequestParam("isAsc") boolean isAsc,
                                                                        @AuthenticationPrincipal UserDetailsImpl user) {
        User loginUser = user.getUser();
        Map<String, Object> crewRecruitmentResponseDtoList = crewRecruitmentService.getMypageCrewRecruitment(
                page - 1,
                size,
                sortBy,
                isAsc,
                loginUser

        );

        return ResponseEntity.ok(crewRecruitmentResponseDtoList);
    }


}








