package com.example.ohchallbe.domain.crewRecruitment.service;

import com.example.ohchallbe.domain.crewRecruitment.dto.CrewApplyResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentRequestDto;
import com.example.ohchallbe.domain.crewRecruitment.dto.CrewRecruitmentResponseDto;
import com.example.ohchallbe.domain.crewRecruitment.entity.CrewRecruitmentEntity;
import com.example.ohchallbe.domain.crewRecruitment.entity.JoinCrewEntity;
import com.example.ohchallbe.domain.crewRecruitment.repository.CrewRecruitmentRepository;
import com.example.ohchallbe.domain.crewRecruitment.repository.JoinCrewRepository;
import com.example.ohchallbe.domain.user.dto.Maildto;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.handler.EmailSender;
import com.example.ohchallbe.global.handler.S3Uploader;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewRecruitmentService {

    private final CrewRecruitmentRepository crewRecruitmentRepository;

    private final JoinCrewRepository joinCrewRepository;

    private final S3Uploader s3Uploader;

    private final EmailSender emailSender;

    public Map<String, Object> getCrewRecruitmentByExerciseKind(String exerciseKind, int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy,"title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CrewRecruitmentEntity> crewRecruitmentListByExerciseKind = crewRecruitmentRepository.findAllByExerciseKind(exerciseKind, pageable);



        if(crewRecruitmentListByExerciseKind.getContent().isEmpty()){
            throw new RuntimeException("존재하지 않는 카테고리입니다."); // 커스텀 익셉션이라 그냥 런타입 익셉션 씀
        }


        Map<String, Object> response = new HashMap<>();
        List<CrewRecruitmentResponseDto> crewRecruitmentResponseDto = crewRecruitmentListByExerciseKind.stream().map(CrewRecruitmentResponseDto::new).collect(Collectors.toList());

        response.put("totalPages", crewRecruitmentListByExerciseKind.getTotalPages());
        response.put("crewList", crewRecruitmentResponseDto);

        return response;
    }

    public  Map<String, Object> getMypageCrewRecruitment(int page, int size, String sortBy, boolean isAsc , User user) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy,"title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CrewRecruitmentEntity> crewList = crewRecruitmentRepository.findCrewRecruitmentEntitiesByUserEmail(user.getEmail(),pageable);


        List<CrewRecruitmentResponseDto> crewRecruitmentResponseDtoList = crewList.stream()
                .map(CrewRecruitmentResponseDto::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", crewList.getTotalPages());
        response.put("crewList", crewRecruitmentResponseDtoList);

        return response;
    }
    public  Map<String, Object> getWholeCrewRecruitment(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy,"title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CrewRecruitmentEntity> crewList = crewRecruitmentRepository.findAll(pageable);


        List<CrewRecruitmentResponseDto> crewRecruitmentResponseDtoList = crewList.stream()
                .map(CrewRecruitmentResponseDto::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", crewList.getTotalPages());
        response.put("crewList", crewRecruitmentResponseDtoList);

        return response;
    }

    public Map<String, Object> searchCrewBasic(String keyword, int page,
                                               int size, String sortBy, boolean isAsc) {
        // 검색 시간 테스트를 위한 코드입니다.

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CrewRecruitmentEntity> searchCrewByKeyWord = crewRecruitmentRepository.searchCrewRecruitmentEntityByKeyWord(keyword, pageable);

        Map<String, Object> response = new HashMap<>();
        List<CrewRecruitmentResponseDto> crewRecruitmentResponseDtoList = searchCrewByKeyWord.stream().map(CrewRecruitmentResponseDto::new).collect(Collectors.toList());

        response.put("totalPages", searchCrewByKeyWord.getTotalPages());
        response.put("crewList", crewRecruitmentResponseDtoList);

        return response;
    }


    public Map<String, Object> searchCrew(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<CrewRecruitmentEntity> crewListByExerciseKind = crewRecruitmentRepository.fullTextSearchCrewByKeyWordNativeVer(
                "+"+keyword+"*",
                pageable.getPageSize(),
                (int)pageable.getOffset()
        );
        Map<String, Object> response = new HashMap<>();
        List<CrewRecruitmentResponseDto> crewRecruitmentResponseDtoList = crewListByExerciseKind.stream().map(CrewRecruitmentResponseDto::new).collect(Collectors.toList());

        int totalCrewCount = crewRecruitmentRepository.countSearchCrewByKeyWordNativeVer("+"+keyword+"*");
        int totalPages = (int) Math.ceil((double) totalCrewCount / size);
        response.put("totalPages", totalPages);

        response.put("totalCrewCount", totalCrewCount);
        response.put("totalPages", totalPages);
        response.put("crewList", crewRecruitmentResponseDtoList);

        return response;

    }



    @Transactional
    public CrewRecruitmentEntity createCrewRecruitment(CrewRecruitmentRequestDto crewRecruitmentRequestDto, User user, List<MultipartFile> images) throws IOException {

        CrewRecruitmentEntity crewRecruitmentEntity;
        if(images == null || images.isEmpty()){



            crewRecruitmentEntity = new CrewRecruitmentEntity(crewRecruitmentRequestDto, new ArrayList<>(), user);

            String nickname = user.getNickname();
            String username = user.getUserName();
            String userEmail = user.getEmail();
//            String userPhoneNumber = user.getPhonenumber();

            JoinCrewEntity joinCrewEntity = new JoinCrewEntity(crewRecruitmentEntity,user,nickname,username,userEmail);
            joinCrewEntity.updateAccepted();
            joinCrewRepository.save(joinCrewEntity);
            return crewRecruitmentRepository.save(crewRecruitmentEntity);
        }else{
            List<String> storedFileName = s3Uploader.upload(images,"imagelist");
            crewRecruitmentEntity = crewRecruitmentRepository.save(new CrewRecruitmentEntity(crewRecruitmentRequestDto,storedFileName,user));
            String nickname = user.getNickname();
            String username = user.getUserName();
            String userEmail = user.getEmail();
            JoinCrewEntity joinCrewEntity = new JoinCrewEntity(crewRecruitmentEntity,user,nickname,username,userEmail);
            joinCrewEntity.updateAccepted();
            joinCrewRepository.save(joinCrewEntity);

            return crewRecruitmentRepository.save(crewRecruitmentEntity);
        }




    }

    @Transactional
    public CrewApplyResponseDto createCrewApply(Long crewRecruitmentId, User user) throws MessagingException {

        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );




        List<JoinCrewEntity> joinCrewEntity  = joinCrewRepository.findByCrewRecruitmentEntity(crewRecruitmentEntity);

        long count = joinCrewEntity.stream().filter(x-> x.getUserEMail().equals(user.getEmail())).count();



        if(crewRecruitmentEntity.getUserEmail().equals(user.getEmail())||count!=0){
            throw new IllegalArgumentException("중복 신청은 불가능합니다");
        }else if(!crewRecruitmentEntity.isFullJoin()){
            String nickname = user.getNickname();
            String username = user.getUserName();
            String userEmail = user.getEmail();
//            String userPhoneNumber = user.getPhonenumber();
            crewRecruitmentEntity.recruitCount();
            crewRecruitmentRepository.save(crewRecruitmentEntity);
            JoinCrewEntity saveJoinCrew = joinCrewRepository.save(new JoinCrewEntity(crewRecruitmentEntity,user,nickname, username, userEmail));



        }else {
            throw new IllegalArgumentException("인원이 가득찼습니다");
        }




        return new CrewApplyResponseDto(crewRecruitmentId,"신청 완료하였습니다");

    }

    @Transactional
    public CrewRecruitmentResponseDto getCrewRecruitment(Long crewRecruitmentId,User user) {
        CrewRecruitmentEntity findCrew = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
        CrewRecruitmentResponseDto crewRecruitmentResponseDto ;

        if(findCrew.getUserEmail().equals(user.getEmail())){
            crewRecruitmentResponseDto = new CrewRecruitmentResponseDto(findCrew);
            crewRecruitmentResponseDto.checkOwner();
            return crewRecruitmentResponseDto;
        } else {
            crewRecruitmentResponseDto = new CrewRecruitmentResponseDto(findCrew);
            return crewRecruitmentResponseDto;
        }



    }

    @Transactional
    public void updateCrewRecruitment(Long crewRecruitmentId, CrewRecruitmentRequestDto crewRecruitmentRequestDto ,User user) {
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );

        if(!(crewRecruitmentEntity.getUserName().equals(user.getUserName())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("작성자와 관리자만 수정할 수 있습니다.");
        }
        String title = crewRecruitmentRequestDto.getTitle();
        String content = crewRecruitmentRequestDto.getContent();
        List<String> images = crewRecruitmentEntity.getImages();
        String crewName = crewRecruitmentRequestDto.getCrewName();

        String exerciseDate = crewRecruitmentRequestDto.getExerciseDate();
//        String endExcerciseDate =crewRecruitmentRequestDto.getEndExerciseDate();
        String location = crewRecruitmentRequestDto.getLocation();
        String usersLocation = crewRecruitmentRequestDto.getUsersLocation();
        String exerciseKind = crewRecruitmentRequestDto.getExerciseKind();
        Long totalNumber = crewRecruitmentEntity.getTotalNumber();

        crewRecruitmentEntity.update(title, content, images, crewName, exerciseDate, location, usersLocation, exerciseKind, totalNumber);
    }


    @Transactional
    public void deleteCrewRecruitment(Long crewRecruitmentId, User user) {
        CrewRecruitmentEntity crewRecruitmentEntity = crewRecruitmentRepository.findById(crewRecruitmentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );
        if (!(crewRecruitmentEntity.getUserName().equals(user.getUserName())) && !(user.getRole().getAuthority().equals("ROLE_ADMIN"))){
            throw new IllegalArgumentException("작성자와 관리자만 삭제할 수 있습니다.");
        }

        crewRecruitmentRepository.delete(crewRecruitmentEntity);
    }

    @Transactional
    public int updateVew(Long id) {
        return crewRecruitmentRepository.updateView(id);
    }



}
