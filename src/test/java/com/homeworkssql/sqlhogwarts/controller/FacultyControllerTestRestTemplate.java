package com.homeworkssql.sqlhogwarts.controller;

import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplate {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate template;

    @Test
    void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty(null, "test_faculty", "test_color");
        ResponseEntity<Faculty> postResponse = template.postForEntity("/faculty", faculty, Faculty.class);
        Faculty addedFaculty = postResponse.getBody();

        var result = template.getForObject("http://localhost:" + port + "/faculty?id=" + addedFaculty.getId(), Faculty.class);
        assertThat(result.getColor()).isEqualTo("test_color");
        assertThat(result.getName()).isEqualTo("test_faculty");

        ResponseEntity<Faculty> resultAfterDelete = template.exchange("/faculty?id=-1", HttpMethod.GET, null, Faculty.class);
        assertThat(resultAfterDelete.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testDelete() {
        Faculty faculty = new Faculty(null, "test_faculty", "test_color");
        ResponseEntity<Faculty> postResponse = template.postForEntity("/faculty", faculty, Faculty.class);
        Faculty addedFaculty = postResponse.getBody();

        var result = template.getForObject("http://localhost:" + port + "/faculty?id=" + addedFaculty.getId(), Faculty.class);
        assertThat(result.getColor()).isEqualTo("test_color");
        assertThat(result.getName()).isEqualTo("test_faculty");

        template.delete("/faculty?id=" + addedFaculty.getId());

        ResponseEntity<Faculty> resultAfterDelete = template.exchange("/faculty?id=" + addedFaculty.getId(), HttpMethod.GET, null, Faculty.class);
        assertThat(resultAfterDelete.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testUpdate() {
        Faculty faculty = new Faculty(null, "test_faculty", "test_color");
        ResponseEntity<Faculty> postResponse = template.postForEntity("/faculty", faculty, Faculty.class);
        Faculty addedFaculty = postResponse.getBody();

        addedFaculty.setName("changed_name");
        addedFaculty.setColor("changed_color");
        template.put("/faculty?id=" + addedFaculty.getId(), addedFaculty);

        var result = template.getForObject("http://localhost:" + port + "/faculty?id=" + addedFaculty.getId(), Faculty.class);
        assertThat(result.getColor()).isEqualTo("changed_color");
        assertThat(result.getName()).isEqualTo("changed_name");
    }
    @Test
    void testFilter() {
        var f1 = template.postForEntity("/faculty", new Faculty(null, "test_name1", "test_color1"), Faculty.class).getBody();
        var f2 = template.postForEntity("/faculty", new Faculty(null, "test_name2", "test_color2"), Faculty.class).getBody();
        var f3 = template.postForEntity("/faculty", new Faculty(null, "test_name3", "test_color3"), Faculty.class).getBody();
        var f4 = template.postForEntity("/faculty", new Faculty(null, "test_name4", "test_color4"), Faculty.class).getBody();

        var faculties = template.getForObject("/faculty/byColorAndName?name=test_name1&color=test_color2", Faculty[].class);
        assertThat(faculties.length).isEqualTo(2);
        assertThat(faculties).containsExactlyInAnyOrder(f1, f2);
    }
    @Test
    void testGetFacultyStudents() {
        var f = new Faculty(null, "test_name1", "test_color1");
        var f1 = template.postForEntity("/faculty", f, Faculty.class).getBody();

        Student newStudent = new Student(null, "s1", 10);
        Student newStudent2 = new Student(null, "s2", 20);
        newStudent.setFaculty(f1);
        newStudent2.setFaculty(f1);
        var s1 = template.postForEntity("/student", newStudent, Student.class).getBody();
        var s2 = template.postForEntity("/student", newStudent2, Student.class).getBody();

        ResponseEntity<List<Student>> result = template.exchange(
                "/faculty/students?facultyId=" + f1.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(result.getBody()).containsExactlyInAnyOrder(
                new Student(1L, "s1", 10),
                new Student(2L, "s2", 20));
    }

}