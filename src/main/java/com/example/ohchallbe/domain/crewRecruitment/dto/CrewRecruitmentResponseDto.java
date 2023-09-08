package com.example.ohchallbe.domain.crewRecruitment.dto;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CrewRecruitmentResponseDto {

    @JsonProperty("crewRecruitmentId")
    private Long crewRecruitmentId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("image")
    private List<String> image;

    @JsonProperty("crewName")
    private String crewName;

    @JsonProperty("postDate")
    private LocalDateTime postDate;

    @JsonProperty("exerciseDate")
    private String excerciseDate;

    @JsonProperty("exerciseTime")
    private String exerciseTime;

//    @JsonProperty("endExerciseDate")
//    private String endExcerciseDate;

    @JsonProperty("location")
    private String location;

    @JsonProperty("usersLocation")
    private String usersLocation;

    @JsonProperty("exerciseKind")
    private String exerciseKind;

    @JsonProperty("totalNumber")
    private Long totalNumber;

    private Integer currentNumber;

    private boolean isOwner =false;



    public CrewRecruitmentResponseDto(CrewRecruitmentEntity crewRecruitmentEntity) {
        this.crewRecruitmentId = crewRecruitmentEntity.getCrewRecruitmentId();
        this.title = crewRecruitmentEntity.getTitle();
        this.content = crewRecruitmentEntity.getContent();
        this.image = crewRecruitmentEntity.getImages();
        this.crewName = crewRecruitmentEntity.getCrewName();
        this.postDate = crewRecruitmentEntity.getCreatePostDate();
        this.excerciseDate = crewRecruitmentEntity.getExerciseDate();
        this.exerciseTime = crewRecruitmentEntity.getExerciseTime();
//        this.endExcerciseDate =crewRecruitmentEntity.getEndExerciseDate();
        this.location = crewRecruitmentEntity.getLocation();
        this.usersLocation = crewRecruitmentEntity.getUsersLocation();
        this.exerciseKind = crewRecruitmentEntity.getExerciseKind();
        this.totalNumber = crewRecruitmentEntity.getTotalNumber();
        this.currentNumber = crewRecruitmentEntity.getCurrentNum();

    }
    public void checkOwner(){
        isOwner = true;
    }
}
