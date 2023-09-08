package com.example.ohchallbe.domain.comment.entity;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.global.entity.Auditing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class CommentEntity extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewRecruitment_id")
    private CrewRecruitmentEntity crewRecruitmentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<CommentEntity> childComments = new ArrayList<>();

//    @OneToMany(mappedBy = "commentEntity", cascade = CascadeType.ALL)
//    private List<CommentLikeEntity> like = new ArrayList<CommentLikeEntity>();


    public CommentEntity(String comment, CrewRecruitmentEntity crewRecruitmentEntity, User user, CommentEntity parentComment) {
        this.comment = comment;
        this.crewRecruitmentEntity = crewRecruitmentEntity;
        this.user = user;
        this.parentComment = parentComment;
    }


    public void update(String comment) {
        this.comment = comment;
    }
}
