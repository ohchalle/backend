package com.example.ohchallbe.domain.comment.repository;

import com.example.ohchallbe.domain.comment.entity.CommentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByCrewRecruitmentEntityAndParentCommentIsNull(CrewRecruitmentEntity entity);
}