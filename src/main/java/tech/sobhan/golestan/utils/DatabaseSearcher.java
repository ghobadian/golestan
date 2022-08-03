package tech.sobhan.golestan.utils;

import java.util.List;

public class DatabaseSearcher<T> {
    public boolean alreadyExists(List<T> allUsers, T user){
        for (T u : allUsers) {
            if(user.equals(u)){
                System.out.println("ERROR403 duplicate Users");
                return true;
            }
        }
        return false;
    }
}
