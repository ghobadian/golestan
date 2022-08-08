package tech.sobhan.golestan.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Slf4j
@Component
public class Util {
    public static void deleteLog(Class clazz, Long id){
        log.info(clazz.getName() + " with id of " + id + "deleted successfully.");
    }
    public static void createLog(Class clazz, Long id){
        log.info(clazz.getName() + " with id of " + id + "created successfully.");
    }

    public static final SecureRandom RANDOM = new SecureRandom();

    public static String generateToken(){
        StringBuilder output = new StringBuilder();
        for(int i=0;i<10;i++){
            output.append(RANDOM.nextBoolean() ? RANDOM.nextInt(10) :
                    RANDOM.nextBoolean() ? randomCapitalChar() : randomSmallChar());
        }
        return String.valueOf(output);
    }

    private static String randomSmallChar(){
        return String.valueOf(Character.valueOf((char) (RANDOM.nextInt(26) + 97)));
    };

    private static String randomCapitalChar(){
        return String.valueOf(Character.valueOf( (char)(RANDOM.nextInt(26) + 65)));
    };
}
