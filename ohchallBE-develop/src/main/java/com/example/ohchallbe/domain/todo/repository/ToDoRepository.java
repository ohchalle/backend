package com.example.ohchallbe.domain.todo.repository;


import com.example.ohchallbe.domain.todo.entity.ToDoEntity;
import com.example.ohchallbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoEntity, Long> {

    List<ToDoEntity> findByUser(User user);
}
