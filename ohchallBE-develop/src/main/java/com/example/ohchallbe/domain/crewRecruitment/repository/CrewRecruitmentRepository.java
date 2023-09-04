package com.example.ohchallbe.domain.crewRecruitment.repository;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRecruitmentRepository extends JpaRepository<CrewRecruitmentEntity, Long> {

    Page<CrewRecruitmentEntity> findAllByExerciseKind(String exerciseKind, Pageable pageable);

    @Query("select n FROM CrewRecruitmentEntity n WHERE n.title like %:keyword% or n.content like %:keyword% or n.exerciseKind like %:keyword%")
    Page<CrewRecruitmentEntity> searchCrewRecruitmentEntityByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM crew_recruitment_entity WHERE MATCH(title,content,exerciseKind) AGAINST (:keyword IN BOOLEAN MODE) "+
                            "LIMIT :limit OFFSET :offset", nativeQuery = true
    )
    List<CrewRecruitmentEntity> fullTextSearchCrewByKeyWordNativeVer(
            @Param("keyword") String keyword,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(
            value = "SELECT COUNT(*) FROM crew_recruitment_entity WHERE MATCH(title,content,exerciseKind) AGAINST (:keyword IN BOOLEAN MODE)",
            nativeQuery = true
    )
    int countSearchCrewByKeyWordNativeVer(
            @Param("keyword") String keyword
    );

    @Modifying
    @Query("update CrewRecruitmentEntity p set p.view = p.view + 1 where p.crewRecruitmentId = :id")
    int updateView(Long id);


    Page<CrewRecruitmentEntity> findCrewRecruitmentEntitiesByUserEmail(String email,Pageable pageable);
    List<CrewRecruitmentEntity> findAllByUserEmail(String email);
}
