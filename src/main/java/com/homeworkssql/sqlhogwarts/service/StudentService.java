package com.homeworkssql.sqlhogwarts.service;

import com.homeworkssql.sqlhogwarts.exceptions.RecordNotFoundException;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student add(Student student) {
        logger.info("Was invoked method for add student: {}", student);
        return repository.save(student);
    }

    public Student get(long id) {
        logger.info("Was invoked method for get student by id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            logger.error("There is no student with id = {}", id);
            return new RecordNotFoundException();
        });
    }

    public boolean delete(long id) {
        logger.info("Was invoked method for delete student by id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                }).orElse(false);
    }

    public Student update(Student student) {
        logger.info("Was invoked method for update student: {}", student);
        return repository.findById(student.getId())
                .map(entity -> repository.save(student))
                .orElse(null);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between: {} and {}", min, max);
        return repository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");
        return repository.findAll();
    }

    public int getCountOfStudents() {
        logger.info("Was invoked method for get count of students");
        return repository.countStudents();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        return repository.avgAge();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return repository.getLastFive();
    }
}
