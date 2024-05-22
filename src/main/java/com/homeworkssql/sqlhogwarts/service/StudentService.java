package com.homeworkssql.sqlhogwarts.service;

import com.homeworkssql.sqlhogwarts.exceptions.RecordNotFoundException;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getNamesStartingWithA() {
        logger.info("Вызван метод для получения имен, начинающихся с 'A'");
        return repository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeOfAllStudents() {
        logger.info("Вызван метод для вычисления среднего возраста всех студентов");
        List<Student> students = repository.findAll();
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);

    }
    public List<Student> getFirstSixStudents() {
        return repository.findAll().stream().limit(6).toList();
    }
    public synchronized void printStudentName(String name) {
        logger.info(name);
    }

    public void printStudentNamesInParallel() {
        List<Student> students = getFirstSixStudents();

        if (students.size() < 6) {
            logger.info("Недостаточно студентов для выполнения задачи.");
            return;
        }

        printStudentName("Main thread: " + students.get(0).getName());
        printStudentName("Main thread: " + students.get(1).getName());

        new Thread(() -> {
            printStudentName("Thread 1: " + students.get(2).getName());
            printStudentName("Thread 1: " + students.get(3).getName());
        }).start();

        new Thread(() -> {
            printStudentName("Thread 2: " + students.get(4).getName());
            printStudentName("Thread 2: " + students.get(5).getName());
        }).start();
    }

    public void printStudentNamesSynchronized() {
        List<Student> students = getFirstSixStudents();

        if (students.size() < 6) {
            logger.info("Недостаточно студентов для выполнения задачи.");
            return;
        }

        printStudentName("Main thread: " + students.get(0).getName());
        printStudentName("Main thread: " + students.get(1).getName());

        new Thread(() -> {
            printStudentName("Thread 1: " + students.get(2).getName());
            printStudentName("Thread 1: " + students.get(3).getName());
        }).start();

        new Thread(() -> {
            printStudentName("Thread 2: " + students.get(4).getName());
            printStudentName("Thread 2: " + students.get(5).getName());
        }).start();
    }

}

