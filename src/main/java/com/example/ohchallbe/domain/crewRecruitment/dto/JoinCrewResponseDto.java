package com.example.ohchallbe.domain.crewRecruitment.dto;

import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import lombok.Getter;

@Getter
public class JoinCrewResponseDto {
    private Long joinCrewId;
    private String nickName;
    private String email;
    private String crewOwnerEmail;
    private String crew;
    private boolean isAccepted;
    private boolean nowSearchisOwner;




    public JoinCrewResponseDto(JoinCrewEntity joinCrewEntity,boolean isOwner) {

        this.nickName = joinCrewEntity.getNickname();
        this.email = joinCrewEntity.getUserEMail();
        this.isAccepted = joinCrewEntity.getIsAccepted();
        this.crew = joinCrewEntity.getCrewRecruitmentEntity().getCrewName();
        this.joinCrewId =joinCrewEntity.getJoinCrewId();
        this.crewOwnerEmail = joinCrewEntity.getUserEMail();
        this.nowSearchisOwner = isOwner;

    }

}
