package com.homeworkssql.sqlhogwarts.controller;

import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public Student get(@RequestParam long id) {
        return service.get(id);
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return service.add(student);
    }

    @DeleteMapping
    public boolean delete(@RequestParam long id) {
        return service.delete(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return service.update(student);
    }

    @GetMapping("/byAge")
    public Collection<Student> getByAgeBetween(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {

        if (min != null && max != null) {
            return service.getByAgeBetween(min, max);
        }
        return service.getAll();
    }

    @GetMapping("faculty")
    public Faculty getStudentFaculty(@RequestParam long studentId) {
        return service.get(studentId).getFaculty();
    }
    @GetMapping("/count")
    public int countStudents() {
        return service.getCountOfStudents();
    }

    @GetMapping("/averageAge")
    public double averageAge() {
        return service.getAverageAge();
    }

    @GetMapping("/lastFive")
    public Collection<Student> getLastFiveStudents() {
        return service.getLastFiveStudents();
    }

}