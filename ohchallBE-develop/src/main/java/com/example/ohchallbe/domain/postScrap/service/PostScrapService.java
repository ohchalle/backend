package com.example.ohchallbe.domain.postScrap.service;

import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.CrewRecruitmentRepository;
import com.example.ohchallbe.domain.postScrap.dto.ScrappedCrewRecruitmentDto;
import com.example.ohchallbe.domain.postScrap.entity.PostScrapEntity;
import com.example.ohchallbe.domain.postScrap.repository.PostScrapRepository;
import com.example.ohchallbe.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostScrapService {

    private final PostScrapRepository postScrapRepository;

    private final CrewRecruitmentRepository crewRecruitmentRepository;

    @Transactional
    public boolean toggleScrap(Long crewRecruitmentId, User user) {
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        boolean isScraped = postScrapRepository.existsScrapsByCrewRecruitmentEntityAndUser(crewRecruitmentEntity, user);

        if (!isScraped) {
            PostScrapEntity scrap = new PostScrapEntity(crewRecruitmentEntity, user);
            postScrapRepository.save(scrap);
        } else {
            PostScrapEntity scrap = postScrapRepository.findByCrewRecruitmentEntityAndUser(crewRecruitmentEntity, user).orElseThrow(
                    () -> new IllegalArgumentException("스크랩에 대한 정보가 존재하지 않습니다.")
            );
            postScrapRepository.delete(scrap);
        }
        return isScraped;
    }

    public List<ScrappedCrewRecruitmentDto> getScrappedCrewRecruitments(User user) {
        List<PostScrapEntity> postScraps = postScrapRepository.findByUser(user);

        List<ScrappedCrewRecruitmentDto> scrappedDtos = postScraps.stream()
                .map(postScrap -> {
                    CrewRecruitmentEntity crewRecruitmentEntity = postScrap.getCrewRecruitmentEntity();
                    return new ScrappedCrewRecruitmentDto(crewRecruitmentEntity);
                })
                .collect(Collectors.toList());

        return scrappedDtos;
    }
}

