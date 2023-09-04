package com.example.ohchallbe.domain.todo.controller;

import com.example.ohchallbe.domain.todo.dto.ToDoRequestDto;
import com.example.ohchallbe.domain.todo.dto.ToDoResponseDto;
import com.example.ohchallbe.domain.todo.repository.ToDoRepository;
import com.example.ohchallbe.domain.todo.service.ToDoService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/mypage/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final ToDoRepository toDoRepository;

    @Operation(summary = "ToDo 생성입니다", description = "ToDo 생성 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto createToDo(@RequestBody ToDoRequestDto toDoRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        toDoService.createToDo(toDoRequestDto, user);
        return new MessageResponseDto("ToDo 생성 성공");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @GetMapping("")
    public List<ToDoResponseDto> getToDo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        return toDoService.getToDo(user);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @PutMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto updateToDo(@PathVariable("toDoId") Long toDoId,
                                         @RequestBody ToDoRequestDto toDoRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        toDoService.updateToDo(toDoId, toDoRequestDto, user);
        return new MessageResponseDto("ToDo 수정 성공");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @DeleteMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto deleteToDo(@PathVariable("toDoId") Long toDoId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        toDoService.deleteToDo(toDoId, user);
        return new MessageResponseDto("ToDo 삭제 성공");
    }
}
