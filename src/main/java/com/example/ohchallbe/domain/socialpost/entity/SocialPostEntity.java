package com.example.ohchallbe.domain.socialpost.entity;

import com.example.ohchallbe.domain.comment.entity.CommentEntity;
import com.example.ohchallbe.domain.socialpost.dto.SocialPostRequestDto;
import com.example.ohchallbe.domain.socialpost.socialcomment.entity.SocialCommentEntity;
import com.example.ohchallbe.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "socialPost")
@NoArgsConstructor
public class SocialPostEntity extends SocialPostTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "socialPost_id")
    private Long socialPostId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "isasc")
    private Boolean isASC;

    @Column(name = "view", columnDefinition = "integer default 0", nullable = false)
    private int view;

    @ElementCollection
    @CollectionTable(name = "social_post_images", joinColumns = @JoinColumn(name = "socialPost_id"))
    @Column(name = "images")
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "socialPost", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<SocialCommentEntity> socialComments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "socialPostEntity", cascade = {CascadeType.REMOVE})
    private List<SocialCommentEntity> socialcommentList = new ArrayList<>();


    public SocialPostEntity(SocialPostRequestDto socialPostRequestDto, List<String> imageUrl, User user){
        this.title = socialPostRequestDto.getTitle();
        this.content = socialPostRequestDto.getContent();
        this.isASC = socialPostRequestDto.getIsASC();
        this.images = imageUrl;
        this.user = user;
    }


    public void update() {
        this.view += 1;
    }
}