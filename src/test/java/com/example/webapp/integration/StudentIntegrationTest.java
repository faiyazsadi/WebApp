package com.example.webapp.integration;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // This ensures each test starts with a clean slate by rolling back
class StudentIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Optional: Manual cleanup if not using @Transactional
        // studentRepository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveStudentFlow() {
        // 1. Create DTO
        StudentDTO dto = new StudentDTO();
        dto.setName("Integration Test Student");
        dto.setRoll("INT-001");

        // 2. Act: Save through Service (This uses ModelMapper + Repository)
        Student savedStudent = studentService.saveStudent(dto);

        // 3. Assert: Verify saved object
        assertNotNull(savedStudent.getId());
        assertEquals("Integration Test Student", savedStudent.getName());

        // 4. Verify: Retrieve from DB using a different Service method
        Optional<Student> fetched = studentService.getStudentById(savedStudent.getId());

        assertTrue(fetched.isPresent());
        assertEquals("INT-001", fetched.get().getRoll());
    }

    @Test
    void testGetAllStudentsFlow() {
        // Arrange: Directly save a few entities to Postgres
        Student s1 = new Student();
        s1.setName("User 1");
        s1.setRoll("R1");
        studentRepository.save(s1);

        Student s2 = new Student();
        s2.setName("User 2");
        s2.setRoll("R2");
        studentRepository.save(s2);

        // Act: Use Service to fetch all
        List<Student> allStudents = studentService.getAllStudents();

        // Assert: Ensure the service sees the real DB data
        // Note: Using >= because other tests might have committed data if @Rollback(false) was used elsewhere
        assertTrue(allStudents.size() >= 2);
    }
}