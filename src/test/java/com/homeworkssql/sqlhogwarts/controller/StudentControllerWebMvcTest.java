package com.homeworkssql.sqlhogwarts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeworkssql.sqlhogwarts.model.Faculty;
import com.homeworkssql.sqlhogwarts.model.Student;
import com.homeworkssql.sqlhogwarts.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetStudent() throws Exception {
        Student student = new Student(1L, "Harry Potter", 17);
        given(studentService.get(1L)).willReturn(student);

        mockMvc.perform(get("/student?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    public void testAddStudent() throws Exception {
        Student newStudent = new Student(null, "Hermione Granger", 17);
        Student savedStudent = new Student(1L, "Hermione Granger", 17);
        given(studentService.add(newStudent)).willReturn(savedStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hermione Granger"));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        given(studentService.delete(1L)).willReturn(true);

        mockMvc.perform(delete("/student?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1L, "Ron Weasley", 18);
        given(studentService.update(updatedStudent)).willReturn(updatedStudent);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.name").value("Ron Weasley"));
    }

    @Test
    public void testGetByAgeBetween() throws Exception {
        mockMvc.perform(get("/student/byAge?min=15&max=20"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);
        student.setFaculty(faculty);

        given(studentService.get(1L)).willReturn(student);

        mockMvc.perform(get("/student/faculty?studentId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }
}