package com.example.ohchallbe.domain.socialpost.socialcomment.entity;

import com.example.ohchallbe.domain.socialpost.entity.SocialPostEntity;
import com.example.ohchallbe.domain.socialpost.socialcomment.dto.SocialCommentRequestDto;
import com.example.ohchallbe.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "socialComment")
@NoArgsConstructor
public class SocialCommentEntity extends SocialCommentTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_comment_id")
    private Long socialCommentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "socialPost_id")

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socialPostEntity_id")
    private SocialPostEntity socialPostEntity;

//
//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "socialPost_id")
//    private SocialPostEntity socialPost;

    public SocialCommentEntity(SocialPostEntity socialPostEntity, User user, String content){
        this.socialPostEntity = socialPostEntity;
        this.user = user;
        this.content = content;
    }

    public void update(SocialCommentRequestDto socialCommentRequestDto) {
        this.content = socialCommentRequestDto.getContent();
    }
}
