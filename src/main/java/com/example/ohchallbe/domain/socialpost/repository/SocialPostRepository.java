package com.example.ohchallbe.domain.socialpost.repository;

import com.example.ohchallbe.domain.socialpost.entity.SocialPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialPostRepository extends JpaRepository<SocialPostEntity, Long> {

//    @Modifying
//    @Query("update SocialPostEntity p set p.view = p.view +1 where p.socialPostId = :id")
//    int updateView(Long Id);

    SocialPostEntity findBySocialPostId(Long socialPostId);

}
