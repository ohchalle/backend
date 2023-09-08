package com.example.ohchallbe.domain.socialpost.dto;

import com.example.ohchallbe.domain.socialpost.entity.SocialPostEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SocialPostResponseDto {

    private Long socialPostId;
    private String nickname;
    private String title;
    private String content;
    private List<String> images;
    private Boolean isASC;
    private LocalDateTime createdAt;

    private boolean isOwner =false;

    public SocialPostResponseDto(SocialPostEntity socialPostEntity){
        this.socialPostId = socialPostEntity.getSocialPostId();
        this.nickname = socialPostEntity.getUser().getNickname();
        this.title = socialPostEntity.getTitle();
        this.content = socialPostEntity.getContent();
        this.images = socialPostEntity.getImages();
        this.isASC = socialPostEntity.getIsASC();
        this.createdAt = socialPostEntity.getCreatedAt();
    }
    public void checkOwner(){
        isOwner = true;
    }

}
