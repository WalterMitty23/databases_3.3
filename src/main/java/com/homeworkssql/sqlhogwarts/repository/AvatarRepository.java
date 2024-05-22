package com.homeworkssql.sqlhogwarts.repository;

import com.homeworkssql.sqlhogwarts.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findAllByStudentId(long studentId);

}
