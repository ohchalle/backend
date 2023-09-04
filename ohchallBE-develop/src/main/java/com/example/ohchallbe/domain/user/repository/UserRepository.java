package com.example.ohchallbe.domain.user.repository;


import com.example.ohchallbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByEmail(String email);
  Optional<User> findByNickname(String nickname);
//  Optional<User> findByPhonenumber(String phoneNumber);
  Optional<User> findByKakaoId(Long kakaoId);
}
