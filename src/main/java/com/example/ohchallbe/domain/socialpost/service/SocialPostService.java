package com.example.ohchallbe.domain.socialpost.service;

import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.socialpost.dto.SocialPostRequestDto;
import com.example.ohchallbe.domain.socialpost.dto.SocialPostResponseDto;
import com.example.ohchallbe.domain.socialpost.entity.SocialPostEntity;
import com.example.ohchallbe.domain.socialpost.repository.SocialPostRepository;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.global.handler.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialPostService {
    private final SocialPostRepository socialPostRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public SocialPostEntity createSocialPost(SocialPostRequestDto socialPostRequestDto, List<MultipartFile> images, User user)throws IOException {

        SocialPostEntity socialPostEntity;
        if(images == null || images.isEmpty()){

            socialPostEntity = new SocialPostEntity(socialPostRequestDto, new ArrayList<>(), user);
            return socialPostRepository.save(socialPostEntity);
        }else{

            List<String> storedFileName = s3Uploader.upload(images,"imagelist");
            socialPostEntity = socialPostRepository.save(new SocialPostEntity(socialPostRequestDto,storedFileName,user));
            return socialPostRepository.save(socialPostEntity);
        }
    }

    @Transactional
    public Map<String, Object> getWholeSocialPost(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy,"title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SocialPostEntity> socialList = socialPostRepository.findAll(pageable);

        List<SocialPostResponseDto> socialPostResponseDtoList = socialList.stream()
                .map(SocialPostResponseDto::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", socialList.getTotalPages());
        response.put("socialList", socialPostResponseDtoList);

        return response;
    }
    @Transactional
    public void deleteSocialPost(Long socialPostId, User user) {
        SocialPostEntity socialPostEntity = socialPostRepository.findById(socialPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        if (!(socialPostEntity.getUser().getUserName().equals(user.getUserName())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))){
            throw new IllegalArgumentException("작성자와 관리자만 삭제할 수 있습니다.");
        }
        socialPostRepository.delete(socialPostEntity);
    }
    @Transactional
    public SocialPostResponseDto getSocialPost(Long socialPostId, User user) {
        SocialPostEntity socialPostEntity = socialPostRepository.findById(socialPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        SocialPostResponseDto socialPostResponseDto;
        if(socialPostEntity.getUser().getEmail().equals(user.getEmail())){
            socialPostResponseDto = new SocialPostResponseDto(socialPostEntity);
            socialPostResponseDto.checkOwner();
            return socialPostResponseDto;
        } else {
            socialPostResponseDto = new SocialPostResponseDto(socialPostEntity);
            return socialPostResponseDto;
        }
    }
    @Transactional
    public void updateView(Long socialPostId) {
        SocialPostEntity postEntity = socialPostRepository.findBySocialPostId(socialPostId);
        postEntity.update();
    }
}