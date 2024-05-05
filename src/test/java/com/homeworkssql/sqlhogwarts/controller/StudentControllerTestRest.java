package com.homeworkssql.sqlhogwarts.controller;

import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetStudent() {
        Student student = new Student(1L, "Harry Potter", 17);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Student result = restTemplate.getForObject("http://localhost:" + port + "/student?id=1", Student.class);
        assertThat(result.getName()).isEqualTo("Harry Potter");
        assertThat(result.getAge()).isEqualTo(17);
    }

    @Test
    public void testAddStudent() {
        Student student = new Student(null, "Hermione Granger", 17);
        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Student result = response.getBody();
        assertThat(result.getName()).isEqualTo("Hermione Granger");
        assertThat(result.getAge()).isEqualTo(17);
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student(1L, "Ron Weasley", 17);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        student.setName("Ronald Weasley");
        restTemplate.put("http://localhost:" + port + "/student", student, Student.class);
        Student updatedStudent = restTemplate.getForObject("http://localhost:" + port + "/student?id=1", Student.class);
        assertThat(updatedStudent.getName()).isEqualTo("Ronald Weasley");
    }
    @Test
    public void testDeleteStudent() {
        Student student = new Student(2L, "Neville Longbottom", 17);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        restTemplate.delete("http://localhost:" + port + "/student?id=2");
        try {
            restTemplate.getForObject("http://localhost:" + port + "/student?id=2", Student.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode().value()).isEqualTo(404);
        }


    }

    @Test
    public void testGetByAgeBetween() {
        restTemplate.postForEntity("http://localhost:" + port + "/student", new Student(null, "Student A", 20), Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student", new Student(null, "Student B", 25), Student.class);
        Student[] results = restTemplate.getForObject("http://localhost:" + port + "/student/byAge?min=18&max=22", Student[].class);
        assertThat(results.length).isEqualTo(1);
        assertThat(results[0].getName()).isEqualTo("Student A");
    }

    @Test
    public void testGetStudentFaculty() {
        Faculty faculty = new Faculty(null, "Gryffindor", "Red");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Faculty savedFaculty = facultyResponse.getBody();

        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(17);
        student.setFaculty(savedFaculty);
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);

        Faculty result = restTemplate.getForObject("http://localhost:" + port + "/student/faculty?studentId=" + studentResponse.getBody().getId(), Faculty.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Gryffindor");
        assertThat(result.getColor()).isEqualTo("Red");
    }
}



