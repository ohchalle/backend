package com.example.ohchallbe.domain.crewRecruitment.dto;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.global.message.MessageResponseDto;
import lombok.Getter;

@Getter
public class AcceptedResponseDto {


    String msg;
    int currnetNum;

    public AcceptedResponseDto(MessageResponseDto messageResponseDto, CrewRecruitmentEntity crewRecruitmentEntity){
        this.msg = messageResponseDto.getMsg();
        this.currnetNum = crewRecruitmentEntity.getCurrentNum();

    }
}
