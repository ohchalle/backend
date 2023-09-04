package com.example.ohchallbe.domain.socialpost.socialcomment.repository;

import com.example.ohchallbe.domain.socialpost.socialcomment.entity.SocialCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialCommentRepository extends JpaRepository<SocialCommentEntity, Long> {
    @Query("select i from SocialCommentEntity i where i.socialPostEntity.socialPostId=:socialPostId")
    List<SocialCommentEntity> findSocialCommentsBySocialPostId(Long socialPostId);

}
