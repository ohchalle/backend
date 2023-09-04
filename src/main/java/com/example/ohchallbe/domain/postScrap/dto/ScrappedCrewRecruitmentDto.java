package com.example.ohchallbe.domain.postScrap.dto;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScrappedCrewRecruitmentDto {

    private Long crewRecruitmentId;

    private String title;

    private String content;

    private List<String> image;

    private String crewName;

    private LocalDateTime postDate;

    private String excerciseDate;

//    @JsonProperty("endExerciseDate")
//    private String endExcerciseDate;

    private String location;

    private String usersLocation;

    private String exerciseKind;

    private Long totalNumber;

    private Integer currentNumber;


    public ScrappedCrewRecruitmentDto(CrewRecruitmentEntity crewRecruitmentEntity) {
        this.crewRecruitmentId = crewRecruitmentEntity.getCrewRecruitmentId();
        this.title = crewRecruitmentEntity.getTitle();
        this.content = crewRecruitmentEntity.getContent();
        this.image = crewRecruitmentEntity.getImages();
        this.crewName = crewRecruitmentEntity.getCrewName();
        this.postDate = crewRecruitmentEntity.getCreatePostDate();
        this.excerciseDate = crewRecruitmentEntity.getExerciseDate();
        this.location = crewRecruitmentEntity.getLocation();
        this.usersLocation = crewRecruitmentEntity.getUsersLocation();
        this.exerciseKind = crewRecruitmentEntity.getExerciseKind();
        this.totalNumber = crewRecruitmentEntity.getTotalNumber();
        this.currentNumber = crewRecruitmentEntity.getCurrentNum();
    }

}
