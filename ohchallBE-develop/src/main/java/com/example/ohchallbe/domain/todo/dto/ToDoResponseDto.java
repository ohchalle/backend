package com.example.ohchallbe.domain.todo.dto;

import com.example.ohchallbe.domain.todo.entity.ToDoEntity;
import lombok.Getter;

@Getter
public class ToDoResponseDto {
    private Long toDoId;

    private String title;

    private String content;

    private String date;

    private Boolean isComplete;

    public ToDoResponseDto(ToDoEntity toDoEntity) {
        this.toDoId = toDoEntity.getToDoId();
        this.title = toDoEntity.getTitle();
        this.content = toDoEntity.getContent();
        this.date = toDoEntity.getDate();
        this.isComplete = toDoEntity.getIsComplete();
    }
}

