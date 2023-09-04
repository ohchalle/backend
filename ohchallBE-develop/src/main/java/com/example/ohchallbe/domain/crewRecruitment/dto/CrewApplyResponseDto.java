package com.example.ohchallbe.domain.crewRecruitment.dto;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrewApplyResponseDto {

    private Long crewRecruitmentId;

    private String msg;

    public CrewApplyResponseDto(Long crewRecruitmentId, String msg) {
        this.crewRecruitmentId = crewRecruitmentId;
        this.msg = msg;
    }

    //    @JsonProperty("joinCrewId")
//    private Long joinCrewId;
//
//    @JsonProperty("nickname")
//    private String nickname;
//
//    @JsonProperty("username")
//    private String username;
//
//    @JsonProperty("userEmail")
//    private String userEmail;
//
//    @JsonProperty("userPhoneNumber")
//    private String userPhoneNumber;

//    public CrewApplyResponseDto(CrewRecruitmentEntity crewRecruitmentEntity) {
//        this.joinCrewId = crewRecruitmentEntity.();
//        this.nickname = crewRecruitmentEntity.get();
//        this.username = joinCrewEntity.getUser().getUserName();
//        this.userEmail = joinCrewEntity.getUser().getEmail();
//        this.userPhoneNumber = joinCrewEntity.getUser().getPhonenumber();
//    }
}
