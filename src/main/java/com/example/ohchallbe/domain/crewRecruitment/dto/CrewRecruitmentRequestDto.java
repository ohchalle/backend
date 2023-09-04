package com.example.ohchallbe.domain.crewRecruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CrewRecruitmentRequestDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

//    @JsonProperty("image")
//    private String image;

    @JsonProperty("crewName")
    private String crewName;


    @JsonProperty("exerciseDate")
    private String exerciseDate;

    private String exerciseTime;

//    @JsonProperty("endExerciseDate")
//    private String endExerciseDate;

    @JsonProperty("location")
    private String location;

    @JsonProperty("usersLocation")
    private String usersLocation;

    @JsonProperty("exerciseKind")
    private String exerciseKind;

    @JsonProperty("totalNumber")
    private Long totalNumber;
}
