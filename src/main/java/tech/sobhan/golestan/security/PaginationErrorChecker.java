package tech.sobhan.golestan.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.PageNumberException;

@Component
@RequiredArgsConstructor
public class PaginationErrorChecker {
    @Value("${defaultMaxInEachPage}")
    private static Integer defaultMaxInEachPage;//todo fix two responses when advising exception

    public static void checkPaginationErrors(int size, Integer pageNumber, Integer maxInEachPage) {
        if(pageNumber ==null)
            if(maxInEachPage < 1) throw new PageNumberException();
            else return;
        if(maxInEachPage == null) maxInEachPage = defaultMaxInEachPage;
        if(pageNumber < 1 || maxInEachPage < 1) throw new PageNumberException();
        if(size < pageNumber * maxInEachPage) throw new PageNumberException();
    }
}
