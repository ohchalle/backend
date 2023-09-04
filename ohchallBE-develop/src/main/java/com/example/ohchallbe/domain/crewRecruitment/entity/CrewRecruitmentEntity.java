package com.example.ohchallbe.domain.crewRecruitment.entity;

import com.example.ohchallbe.domain.comment.entity.CommentEntity;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentRequestDto;
import com.example.ohchallbe.domain.postScrap.entity.PostScrapEntity;
import com.example.ohchallbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class CrewRecruitmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crewRecruitment_id")
    private Long crewRecruitmentId;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "crew_recruitment_images", joinColumns = @JoinColumn(name = "crew_recruitment_id"))
    @Column(name = "image")
    private List<String> images = new ArrayList<>();

    @Column(name = "crewName")
    private String crewName;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createPostDate;

    @Column(name = "exerciseDate")
    private String exerciseDate;

    @Column(name = "exerciseTime")
    private String exerciseTime;

//    @Column(name = "endExerciseDate")
//    private String endExerciseDate;

    @Column(name = "location")
    private String location;

    @Column(name = "usersLocation")
    private String usersLocation;

    @Column(name = "exerciseKind", columnDefinition = "TEXT")
    private String exerciseKind;

    @Column(name = "totalNumber")
    private Long totalNumber;

    @Column(name = "userName")
    private String userName;

//    @Column(name = "userPhoneNumber")
//    private String userPhoneNumber;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "view", columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(name = "recruit_count", nullable = false)
    private int currentNum = 1;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;


    @OneToMany(mappedBy = "crewRecruitmentEntity", cascade = CascadeType.ALL)
    private List<JoinCrewEntity> joinCrewEntity = new ArrayList<JoinCrewEntity>();


    @OneToMany(mappedBy = "crewRecruitmentEntity", cascade = CascadeType.ALL)
    private List<PostScrapEntity> scrap = new ArrayList<PostScrapEntity>();

    @OneToMany(mappedBy = "crewRecruitmentEntity", cascade = CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();


    public CrewRecruitmentEntity(CrewRecruitmentRequestDto crewRecruitmentRequestDto, List<String> imageUrl, User user) {
        this.title = crewRecruitmentRequestDto.getTitle();
        this.content = crewRecruitmentRequestDto.getContent();
        this.images = imageUrl;
        this.crewName = crewRecruitmentRequestDto.getCrewName();
        this.exerciseDate = crewRecruitmentRequestDto.getExerciseDate();
        this.exerciseTime = crewRecruitmentRequestDto.getExerciseTime();
        this.location = crewRecruitmentRequestDto.getLocation();
        this.usersLocation = crewRecruitmentRequestDto.getUsersLocation();
        this.exerciseKind = crewRecruitmentRequestDto.getExerciseKind();
        this.totalNumber = crewRecruitmentRequestDto.getTotalNumber();
        this.userName = user.getUserName();
//            this.userPhoneNumber = user.getPhonenumber();
        this.userEmail = user.getEmail();


//        return CrewRecruitmentEntity.builder()
//                .title(crewRecruitmentRequestDto.getTitle())
//                .content(crewRecruitmentRequestDto.getContent())
//
//                .images(imageUrl)
//
//                .crewName(crewRecruitmentRequestDto.getCrewName())
////                .startExerciseDate(crewRecruitmentRequestDto.getStartExerciseDate())
//                .exerciseDate(crewRecruitmentRequestDto.getExerciseDate())
//                .location(crewRecruitmentRequestDto.getLocation())
//                .usersLocation(crewRecruitmentRequestDto.getUsersLocation())
//                .exerciseKind(crewRecruitmentRequestDto.getExerciseKind())
//                .totalNumber(crewRecruitmentRequestDto.getTotalNumber())
//                .userName(user.getUserName())
//                .userPhoneNumber(user.getPhonenumber())
//                .userEmail(user.getEmail())
//                .build();
    }

    public void update(String title, String content, List<String> images, String crewName, String exerciseDate, String location, String usersLocation, String exerciseKind, Long totalNumber) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.crewName = crewName;
        this.exerciseDate = exerciseDate;
//        this.endExerciseDate = endExerciseDate;
        this.location = location;
        this.usersLocation = usersLocation;
        this.exerciseKind = exerciseKind;
        this.totalNumber = totalNumber;

    }

    public void recruitCount() {
        currentNum++;
    }

    public boolean isFullJoin() {
        if (totalNumber > currentNum) {
            return false;
        } else {
            return true;
        }
    }
}