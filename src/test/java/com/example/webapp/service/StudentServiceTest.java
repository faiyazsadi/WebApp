package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getAllStudents_shouldReturnStudents() {
        Student s1 = new Student();
        s1.setName("Rahim");

        when(studentRepository.findAll()).thenReturn(List.of(s1));

        List<Student> students = studentService.getAllStudents();

        assertEquals(1, students.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Optional<Student> result = studentService.getStudentById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void saveStudent_shouldMapAndSave() {
        StudentDTO dto = new StudentDTO();
        dto.setName("Karim");
        dto.setRoll("CSE-01");

        Student mappedStudent = new Student();
        mappedStudent.setName("Karim");
        mappedStudent.setRoll("CSE-01");

        when(modelMapper.map(dto, Student.class))
                .thenReturn(mappedStudent);

        when(studentRepository.save(mappedStudent))
                .thenReturn(mappedStudent);

        Student saved = studentService.saveStudent(dto);

        assertEquals("Karim", saved.getName());
        verify(modelMapper).map(dto, Student.class);
        verify(studentRepository).save(mappedStudent);
    }
}
