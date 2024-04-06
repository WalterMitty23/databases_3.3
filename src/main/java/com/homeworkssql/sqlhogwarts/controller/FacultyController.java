package com.homeworkssql.sqlhogwarts.controller;

import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @GetMapping
    public Faculty get(@RequestParam long id) {
        return service.get(id);
    }

    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return service.add(faculty);
    }

    @DeleteMapping
    public boolean delete(@RequestParam long id) {
        return service.delete(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return service.update(faculty);
    }

    @GetMapping("/byColorAndName")
    public Collection<Faculty> getByColor(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String name) {

        if (color == null && name == null) {
            return service.getAll();
        }
        return service.getByColorOrName(color, name);
    }

      @GetMapping("students")
    public List<Student> getStudentFaculty(@RequestParam long facultyId) {
        return service.get(facultyId).getStudents();
    }
}