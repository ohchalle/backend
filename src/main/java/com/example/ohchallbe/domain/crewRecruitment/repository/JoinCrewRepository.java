package com.example.ohchallbe.domain.crewRecruitment.repository;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinCrewRepository extends JpaRepository<JoinCrewEntity, Long> {


    List<JoinCrewEntity> findJoinCrewEntitiesByCrewRecruitmentEntityIn(List<CrewRecruitmentEntity> crewRecruitmentEntities);
    JoinCrewEntity findByUserEMail(String email);
    List<JoinCrewEntity> findByCrewRecruitmentEntity(CrewRecruitmentEntity crewRecruitmentEntity);
}
