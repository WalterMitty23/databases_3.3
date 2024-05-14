package com.homeworkssql.sqlhogwarts.service;

import com.homeworkssql.sqlhogwarts.exceptions.RecordNotFoundException;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student add(Student student) {
        return repository.save(student);
    }

    public Student get(long id) {
        return repository.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    public boolean delete(long id) {
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                }).orElse(false);
    }

    public Student update(Student student) {
        return repository.findById(student.getId())
                .map(entity -> repository.save(student))
                .orElse(null);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        return repository.findAllByAgeBetween(min, max);
    }
    public Collection<Student> getAll() {
        return repository.findAll();

    }
    public int getCountOfStudents() {
        return repository.countStudents();
    }

    public double getAverageAge() {
        return repository.avgAge();
    }

    public Collection<Student> getLastFiveStudents() {
        return repository.getLastFive();
    }
}