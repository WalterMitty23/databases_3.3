package com.homeworkssql.sqlhogwarts.service;

import com.homeworkssql.sqlhogwarts.exceptions.RecordNotFoundException;
import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty add(Faculty faculty) {
        logger.info("Was invoked method for add faculty: {}", faculty);
        return repository.save(faculty);
    }

    public Faculty get(long id) {
        logger.info("Was invoked method for get faculty by id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            logger.error("There is no faculty with id = {}", id);
            return new RecordNotFoundException();
        });
    }

    public boolean delete(long id) {
        logger.info("Was invoked method for delete faculty by id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                }).orElse(false);
    }

    public Faculty update(Faculty faculty) {
        logger.info("Was invoked method for update faculty: {}", faculty);
        return repository.findById(faculty.getId())
                .map(entity -> repository.save(faculty))
                .orElse(null);
    }

    public Collection<Faculty> getByColorOrName(String color, String name) {
        logger.info("Was invoked method for get faculty by color or name: {} or {}", color, name);
        return repository.findAllByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Collection<Faculty> getAll() {
        logger.info("Was invoked method for get all faculties");
        return repository.findAll();
    }
    public String getLongestFacultyName() {
        logger.info("Was invoked method for get the longest faculty name");
        return repository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

}
