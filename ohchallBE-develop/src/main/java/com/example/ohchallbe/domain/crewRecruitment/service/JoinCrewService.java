package com.example.ohchallbe.domain.crewRecruitment.service;

import com.example.ohchallbe.domain.crewRecruitment.dto.JoinCrewResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.CrewRecruitmentRepository;
import com.example.ohchallbe.domain.crewRecruitment.repository.JoinCrewRepository;
import com.example.ohchallbe.domain.user.dto.Maildto;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.handler.EmailSender;
import com.example.ohchallbe.global.message.MessageResponseDto;
import com.example.ohchallbe.global.security.UserDetailsImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JoinCrewService {

    private final JoinCrewRepository joinCrewRepository;
    private final CrewRecruitmentRepository crewRecruitmentRepository;
    private final EmailSender emailSender;

    public List<JoinCrewResponseDto> getJoinCrewList(String userEmail, Boolean isAccepted) {


        List<CrewRecruitmentEntity> crewRecruitmentEntityList = crewRecruitmentRepository.findAllByUserEmail(userEmail);

        List<Long> crewRecruitmentEntityIds = crewRecruitmentEntityList.stream()
                .map(CrewRecruitmentEntity::getCrewRecruitmentId)
                .collect(Collectors.toList());



        List<JoinCrewEntity> joinCrewEntities;

        if (isAccepted.equals(Boolean.FALSE)) {

            joinCrewEntities = joinCrewRepository.findJoinCrewEntitiesByCrewRecruitmentEntityIn(crewRecruitmentEntityList).stream()
                    .filter(joinCrewEntity -> !joinCrewEntity.getIsAccepted()).collect(Collectors.toList());

        }else{
            joinCrewEntities = joinCrewRepository.findJoinCrewEntitiesByCrewRecruitmentEntityIn(crewRecruitmentEntityList).stream()
                    .filter(joinCrewEntity -> joinCrewEntity.getIsAccepted()).collect(Collectors.toList());
        }


        return joinCrewEntities.stream().map(x-> new JoinCrewResponseDto(x,false)).collect(Collectors.toList());
    }

    public void AcceptJoinCrewApply(String userEmail,Long joinCrewId){

       JoinCrewEntity joinCrewEntity = joinCrewRepository.findById(joinCrewId).orElseThrow(()->new IllegalArgumentException("해당 신청이 존재하지 않습니다"));

       String crewOwnerEmail = joinCrewEntity.getCrewRecruitmentEntity().getUserEmail();

       if (crewOwnerEmail.equals(userEmail)){
           joinCrewEntity.updateAccepted();
           joinCrewRepository.save(joinCrewEntity);
       }else{
           throw new IllegalArgumentException("해당 크루게시글 작성자가 아닙니다");
       }

    }




    public List<JoinCrewResponseDto> getDetailPageJoinCrewList(Long crewRecruitmentEntityId , Boolean isAccepted, UserDetailsImpl userDetails) {



        boolean isOwner;

        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentEntityId).orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        if(crewRecruitmentEntity.getUserEmail().equals(userDetails.getUser().getEmail())){
            isOwner = true;
        } else {
            isOwner = false;
        }
        List<JoinCrewEntity> joinCrewEntities ;




        if (isAccepted.equals(Boolean.FALSE)) {

            joinCrewEntities = joinCrewRepository.findByCrewRecruitmentEntity(crewRecruitmentEntity).stream()
                    .filter(joinCrewEntity -> !joinCrewEntity.getIsAccepted()).collect(Collectors.toList());

        }else{
            joinCrewEntities = joinCrewRepository.findByCrewRecruitmentEntity(crewRecruitmentEntity).stream()
                    .filter(joinCrewEntity -> joinCrewEntity.getIsAccepted().equals(true)).collect(Collectors.toList());
        }


        return joinCrewEntities.stream().map(x->new JoinCrewResponseDto(x,isOwner)).collect(Collectors.toList());
    }

    public MessageResponseDto acceptDetailPageJoinCrewApply(String userEmail, Long crewRecruitmenEntitytId, String applyerEmail) throws MessagingException {

        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmenEntitytId).orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        List<JoinCrewEntity> joinCrewEntities = joinCrewRepository.findByCrewRecruitmentEntity(crewRecruitmentEntity);

        JoinCrewEntity joinCrewEntity_1 = joinCrewEntities.stream()
                .filter(joinCrewEntity -> joinCrewEntity.getUserEMail().equals(applyerEmail)).findFirst().orElseThrow(()->new IllegalArgumentException("해당 신청자가 존재하지 않습니다"));

        String crewOwnerEmail =  joinCrewEntity_1.getCrewRecruitmentEntity().getUserEmail();


        if (crewOwnerEmail.equals(userEmail)){

            joinCrewEntity_1.updateAccepted();
            joinCrewRepository.save(joinCrewEntity_1);
            Maildto maildto = Maildto.builder().title(joinCrewEntity_1.getNickname() + "님을 수락하였습니다")
                    .message(joinCrewEntity_1.getNickname()+"("+joinCrewEntity_1.getUserEMail()+")님을"+ LocalTime.now()+" 크루 수락 하셨습니다" +joinCrewEntity_1.getCrewRecruitmentEntity().getExerciseKind()+"같이 하고 싶으시다면" +
                            "지금 ohchalle 방문해주세요")
                    .toAddress(crewRecruitmentEntity.getUserEmail()).build();
            emailSender.sendMail(maildto);

            return MessageResponseDto.builder().msg("해당 신청자를 수락하셨습니다").build();


        }else{

            return MessageResponseDto.builder().msg("작성자만 수락할 수 있습니다").build();
        }

    }

}
