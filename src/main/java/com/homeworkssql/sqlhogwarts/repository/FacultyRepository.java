package com.homeworkssql.sqlhogwarts.repository;

import com.homeworkssql.sqlhogwarts.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    Collection<Faculty> findAllByColorAndNameIgnoreCase(String color, String name);
    Collection<Faculty> findAllByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

}
