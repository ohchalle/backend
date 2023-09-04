package com.example.ohchallbe.domain.crewRecruitment.entity;

import com.example.ohchallbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class JoinCrewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinCrewId;

    private String nickname;

    private String username;

    private String userEMail;

//    private String userPhoneNumber;

    private Boolean isAccepted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewRecruitment_id")
    private CrewRecruitmentEntity crewRecruitmentEntity;


    public JoinCrewEntity(CrewRecruitmentEntity crewRecruitmentEntity,User user, String nickname, String username, String userEmail) {

        this.user = user;
        this.nickname = nickname;
        this.username = username;
        this.userEMail = userEmail;
//        this.userPhoneNumber = userPhoneNumber;
        this.crewRecruitmentEntity = crewRecruitmentEntity;
    }

    public void updateAccepted(){
        isAccepted = true;
    }

//    public void createJoinCrew(String nickname, String username, String userEmail, String userPhoneNumber) {
//        this.user.getNickname() = nickname;
//        this.user.getUserName() = username;
//        this.user.getEmail() = userEmail;
//        this.user.getPhoneNumber() = userPhoneNumber;
//
//    }
}
