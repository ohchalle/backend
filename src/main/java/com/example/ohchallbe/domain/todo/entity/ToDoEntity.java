package com.example.ohchallbe.domain.todo.entity;

import com.example.ohchallbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ToDoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long toDoId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @Column(name = "isComplete")
    private Boolean isComplete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    public ToDoEntity(Long toDoId, String title, String content, String date) {
//        this.toDoId = toDoId;
//        this.title = title;
//        this.content = content;
//        this.date = date;
//    }

    public ToDoEntity(String title, String content, String date, Boolean isComplete, User user) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isComplete = isComplete;
        this.user = user;
    }

    public void update(String title, String content, String date, Boolean isComplete) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isComplete = isComplete;
    }
}
