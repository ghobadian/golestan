package tech.sobhan.golestan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
public class MultipleMarksTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private final RepositoryHandler repositoryHandler = Mockito.mock(RepositoryHandler.class);
    private static MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        try{
            repositoryHandler.findUserByUsername("admin");
        }catch (UserNotFoundException e){
            User admin = User.builder().username("admin").password("admin").name("admin").phone("1234")
                    .nationalId("1234").admin(true).active(true).build();
            repositoryHandler.saveUser(admin);
        }
    }

    public void test(){

    }
}
