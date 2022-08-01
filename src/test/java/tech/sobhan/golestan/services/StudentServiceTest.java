package tech.sobhan.golestan.services;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentServiceTest {
    @Autowired
    private final StudentService studentService = Mockito.mock(StudentService.class);


    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {
        String response = studentService.seeScoresInSpecifiedTerm(26L, "msghobadian", "abcd1234");
        double avg = Double.parseDouble(response.substring(12,14));
        assertEquals(avg, 10);
    }
}