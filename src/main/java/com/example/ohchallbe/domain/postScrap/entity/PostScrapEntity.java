package com.example.ohchallbe.domain.postScrap.entity;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostScrapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_scrap_id")
    private Long postScrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewRecruitment_id")
    private CrewRecruitmentEntity crewRecruitmentEntity;

    public PostScrapEntity(CrewRecruitmentEntity crewRecruitmentEntity, User user) {
        this.crewRecruitmentEntity = crewRecruitmentEntity;
        this.user = user;
        crewRecruitmentEntity.getScrap().add(this);
    }
}
