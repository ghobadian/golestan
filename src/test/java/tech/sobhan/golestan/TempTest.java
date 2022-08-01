package tech.sobhan.golestan;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

public class TempTest {
    @Test
    public void tempTest() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("ls ");
        System.out.println(pr.getOutputStream());
    }
}
