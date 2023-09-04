package com.example.ohchallbe.domain.socialpost.socialcomment.dto;

import com.example.ohchallbe.domain.socialpost.socialcomment.entity.SocialCommentEntity;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class SocialCommentResponseDto {
    private Long socialCommentId;
    private String content;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SocialCommentResponseDto(SocialCommentEntity socialCommentEntity){
        this.socialCommentId = socialCommentEntity.getSocialCommentId();
        this.content = socialCommentEntity.getContent();
        this.nickname = socialCommentEntity.getUser().getNickname();
        this.email = socialCommentEntity.getUser().getEmail();
        this.createdAt = socialCommentEntity.getCreatedAt();
        this.modifiedAt = socialCommentEntity.getModifiedAt();
//        this.nickname = user.getNickname(); //이방식은 왜 안되는건지?stream을 쓸줄 알면 왜 안되는지 알 수있다.
    }//자바기본강의 5주차쪽 참고, 그냥 인터넷 구글링 ㄱㄱ
}
