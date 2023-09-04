package com.example.ohchallbe.domain.comment.dto;

import com.example.ohchallbe.domain.comment.entity.CommentEntity;
import com.example.ohchallbe.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long commentId; // 작성자 이름

    private String nickname;

    private String comment;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> childComments;

    private boolean isUserAComment;

    public CommentResponseDto(CommentEntity commentEntity, String currentUserEmail) {
        this.commentId = commentEntity.getCommentId();
        this.nickname = commentEntity.getUser().getNickname();
        this.comment = commentEntity.getComment();
        this.createdAt = commentEntity.getCreatedAt();
        this.isUserAComment = commentEntity.getUser().getEmail().equals(currentUserEmail);

        List<CommentResponseDto> childDtos = new ArrayList<>();
        for (CommentEntity child : commentEntity.getChildComments()) {
            childDtos.add(new CommentResponseDto(child, currentUserEmail));
        }
        this.childComments = childDtos;
    }
}
