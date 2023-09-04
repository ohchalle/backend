package com.example.ohchallbe.domain.socialpost.socialcomment.service;


import com.example.ohchallbe.domain.socialpost.entity.SocialPostEntity;
import com.example.ohchallbe.domain.socialpost.repository.SocialPostRepository;
import com.example.ohchallbe.domain.socialpost.socialcomment.dto.SocialCommentRequestDto;
import com.example.ohchallbe.domain.socialpost.socialcomment.dto.SocialCommentResponseDto;
import com.example.ohchallbe.domain.socialpost.socialcomment.entity.SocialCommentEntity;
import com.example.ohchallbe.domain.socialpost.socialcomment.repository.SocialCommentRepository;
import com.example.ohchallbe.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialCommentService {
    private final SocialCommentRepository socialCommentRepository;
    private final SocialPostRepository socialPostRepository;

    public SocialCommentResponseDto createComment(Long socialPostId, User user, SocialCommentRequestDto socialCommentRequestDto) {
        SocialPostEntity socialPostEntity = socialPostRepository.findById(socialPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 sns 게시글 입니다.")
        );
        //RequestDto -> Entity
        SocialCommentEntity socialCommentEntity = new SocialCommentEntity(socialPostEntity, user,socialCommentRequestDto.getContent());
        //DB저장
        SocialCommentEntity saveComment = socialCommentRepository.save(socialCommentEntity);
        //Entity -> ResponseDto
        SocialCommentResponseDto socialCommentResponseDto = new SocialCommentResponseDto(saveComment);
        return socialCommentResponseDto;
    }

    public List<SocialCommentResponseDto> getSocialCommentsBySocialPostId(Long socialPostId) {
        SocialPostEntity socialPostEntity = socialPostRepository.findById(socialPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 sns 게시글 입니다.")
        );
        //DB조회
//        return socialCommentRepository.findCommentsBySocialPostId(socialPostId).stream().map(SocialCommentResponseDto::new).toList();
        return  socialCommentRepository.findSocialCommentsBySocialPostId(socialPostId).stream().map(SocialCommentResponseDto::new).toList();
    }

    @Transactional
    public SocialCommentResponseDto updateComment(Long socialCommentId, User user, SocialCommentRequestDto socialCommentRequestDto) {
        SocialCommentEntity socialCommentEntity = socialCommentRepository.findById(socialCommentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 sns 댓글 입니다.")
        );
        if(!(socialCommentEntity.getUser().getUserName().equals(user.getUserName())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("작성자와 관리자만 수정할 수 있습니다.");
        }
        //DB수정
        socialCommentEntity.update(socialCommentRequestDto);
        //Entity -> ResponseDto
        SocialCommentResponseDto socialCommentResponseDto = new SocialCommentResponseDto(socialCommentEntity);
        return socialCommentResponseDto;
    }

    public Boolean deleteComment(Long socialCommentId,User user) {
        SocialCommentEntity socialCommentEntity = socialCommentRepository.findById(socialCommentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 sns 댓글 입니다.")
        );
        if(!(socialCommentEntity.getUser().getUserName().equals(user.getUserName())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("작성자와 관리자만 삭제할 수 있습니다.");
        }

        socialCommentRepository.delete(socialCommentEntity);//이게 왜 삭제가 되는거지?
        return true;
    }
}
