package com.example.ohchallbe.domain.postScrap.repository;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.postScrap.entity.PostScrapEntity;
import com.example.ohchallbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScrapEntity, Long> {

    boolean existsScrapsByCrewRecruitmentEntityAndUser(CrewRecruitmentEntity crewRecruitmentEntity, User user);

    Optional<PostScrapEntity> findByCrewRecruitmentEntityAndUser(CrewRecruitmentEntity crewRecruitmentEntity, User user);

    List<PostScrapEntity> findByUser(User user);
}
