package com.example.ohchallbe.domain.todo.service;

import com.example.ohchallbe.domain.todo.dto.ToDoRequestDto;
import com.example.ohchallbe.domain.todo.dto.ToDoResponseDto;
import com.example.ohchallbe.domain.todo.entity.ToDoEntity;
import com.example.ohchallbe.domain.todo.repository.ToDoRepository;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;
//    private final UserRepository userRepository;

    //ToDo 생성
    @Transactional
    public ToDoResponseDto createToDo(ToDoRequestDto toDoRequestDto, User user) {
        String title = toDoRequestDto.getTitle();
        String content = toDoRequestDto.getContent();
        String date = toDoRequestDto.getDate();
        Boolean isComplete = toDoRequestDto.getIsComplete();

        ToDoEntity toDoEntity = toDoRepository.save(new ToDoEntity(title, content, date, isComplete, user));

        return new ToDoResponseDto(toDoEntity); // 해결 필요
    }

    //ToDo 조회
    public List<ToDoResponseDto> getToDo(User user) {
        return toDoRepository.findByUser(user).stream().map(ToDoResponseDto::new).toList();
    }

    //ToDo 수정
    @Transactional
    public void updateToDo(Long toDoId, ToDoRequestDto toDoRequestDto, User user) { // 여기 유저 바꿔야함 +
        ToDoEntity toDoEntity = toDoRepository.findById(toDoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 ToDo 입니다.")
        );

        String title = toDoRequestDto.getTitle();
        String content = toDoRequestDto.getContent();
        String date = toDoRequestDto.getDate();
        Boolean isComplete = toDoRequestDto.getIsComplete();

        toDoEntity.update(title, content, date, isComplete);

    }


    //ToDo 삭제
    @Transactional
    public void deleteToDo(Long toDoId, User user) { // 유저
        ToDoEntity toDoEntity = toDoRepository.findById(toDoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 ToDo 입니다.")
        );

        toDoRepository.delete(toDoEntity);
    }

    private ToDoEntity findToDo(Long toDoId) {
        return toDoRepository.findById(toDoId).orElseThrow(() -> new IllegalArgumentException("선택한 ToDo는 존재하지 않습니다."));
    }
}