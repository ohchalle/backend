package com.example.ohchallbe.domain.todo.dto;

import lombok.Getter;

@Getter
public class ToDoRequestDto {
    private String title;
    private String content;
    private String date;
    private Boolean isComplete;

//    public ToDoRequestDto(String title, String content, String date, Boolean isComplete) { 다현
//        this.title = title;
//        this.content = content;
//        this.date = date;
//        this.isComplete = isComplete;
//    }
}
