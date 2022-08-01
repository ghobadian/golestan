package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentServiceTest {
    @Autowired
    private final StudentService studentService = Mockito.mock(StudentService.class);


    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {
        JSONArray response = studentService.seeScoresInSpecifiedTerm(26L, "msghobadian", "abcd1234");
        double avg = (Double) ((JSONObject) response.get(0)).get("average");
        assertEquals(avg, 20/3.0);
    }
}