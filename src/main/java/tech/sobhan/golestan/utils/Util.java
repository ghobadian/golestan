package tech.sobhan.golestan.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Util {
    public static void deleteLog(Class clazz, Long id){
        log.info(clazz.getName() + " with id of " + id + "deleted successfully.");
    }
    public static void createLog(Class clazz, Long id){
        log.info(clazz.getName() + " with id of " + id + "created successfully.");
    }
}
