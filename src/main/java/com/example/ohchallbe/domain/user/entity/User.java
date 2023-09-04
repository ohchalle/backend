package com.example.ohchallbe.domain.user.entity;


import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.postScrap.entity.PostScrapEntity;
import com.example.ohchallbe.domain.user.dto.MyPageUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

//    @Column(nullable = false, unique = true)
//    private String phonenumber;

    @CurrentTimestamp
    @Column(updatable = true)
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JoinCrewEntity> joinCrewEntity = new ArrayList<JoinCrewEntity>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostScrapEntity> postScrapEntity = new ArrayList<PostScrapEntity>();

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    public void updateUser(MyPageUpdateRequestDto myPageUpdateRequestDto) {
        this.nickname = myPageUpdateRequestDto.getNickname();
        this.userName = myPageUpdateRequestDto.getUserName();
        this.email = myPageUpdateRequestDto.getUserEmail();
//        this.phonenumber = myPageUpdateRequestDto.getUserPhoneNumber();
        this.modifiedAt = LocalDateTime.now();
    }

    public User(String nickname,String password ,String email, UserRoleEnum role,String userName){
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
//        this.phonenumber = phonenumber;
        this.userName = userName;
    }

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.userName = username;
        this.nickname = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }
    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;

        return this;
    }
}
