package com.example.ohchallbe.domain.comment.service;

import com.example.ohchallbe.domain.comment.dto.CommentRequestDto;
import com.example.ohchallbe.domain.comment.dto.CommentResponseDto;
import com.example.ohchallbe.domain.comment.entity.CommentEntity;
import com.example.ohchallbe.domain.comment.repository.CommentRepository;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.CrewRecruitmentRepository;
import com.example.ohchallbe.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CrewRecruitmentRepository crewRecruitmentRepository;

    @Transactional
    public List<CommentResponseDto> getComments(Long crewRecruitmentId, String currentUserEmail) {
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        List<CommentEntity> rootComments = commentRepository.findAllByCrewRecruitmentEntityAndParentCommentIsNull(
                crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                        ()-> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
                )
        );
        for (CommentEntity rootComment : rootComments) {
            commentDtos.add(new CommentResponseDto(rootComment, currentUserEmail));
        }
        return commentDtos;
    }

    @Transactional
    public CommentEntity createComment(Long crewRecruitmentId, CommentRequestDto commentRequestDto, User user) {
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        String comment = commentRequestDto.getComment();

        CommentEntity commentEntity = commentRepository.save(new CommentEntity(comment, crewRecruitmentEntity, user, null));

        return commentEntity;
    }
    @Transactional
    public CommentEntity createChildComment(Long crewRecruitmentId, Long parentCommentId, CommentRequestDto commentRequestDto, User user) {
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        CommentEntity parentComment = commentRepository.findById(parentCommentId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
        String comment = commentRequestDto.getComment();

        CommentEntity childComment = new CommentEntity(comment, crewRecruitmentEntity, user, parentComment);
        parentComment.getChildComments().add(childComment);

        return commentRepository.save(childComment);
    }

    @Transactional
    public CommentEntity updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
        if (!(commentEntity.getUser().getNickname().equals(user.getNickname())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("작성자와 관리자만 수정할 수 있습니다.");
        }

        String comment = commentRequestDto.getComment();

        commentEntity.update(comment);

        return commentEntity;
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        CommentEntity commentEntity =commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
        if (!(commentEntity.getUser().getNickname().equals(user.getNickname()) && !(user.getRole().getAuthority().equals("ADMIN")))) {
            throw new IllegalArgumentException("작성자와 관리자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(commentEntity);
    }

}