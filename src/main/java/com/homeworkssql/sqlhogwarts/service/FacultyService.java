package com.homeworkssql.sqlhogwarts.service;
import com.homeworkssql.sqlhogwarts.exceptions.RecordNotFoundException;
import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository repository;


    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty add(Faculty faculty) {
        return repository.save(faculty);
    }

    public Faculty get(long id) {
        return repository.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    public boolean delete(long id) {
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                }).orElse(false);
    }
    public Faculty update(Faculty faculty) {
        return repository.findById(faculty.getId())
                .map(entity -> repository.save(faculty))
                .orElse(null);
    }

    public Collection<Faculty> getByColorOrName(String color,String name) {
        return repository.findAllByColorIgnoreCaseOrNameIgnoreCase(color,name);
    }
    public Collection<Faculty> getAll() {
        return repository.findAll();
    }

}
