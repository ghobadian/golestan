package tech.sobhan.golestan.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tech.sobhan.golestan.auth.CustomUserDetailsService;

import java.util.concurrent.TimeUnit;

import static tech.sobhan.golestan.constants.ApiPaths.MODIFY_ROLE_PATH;
import static tech.sobhan.golestan.security.Role.ADMIN;
import static tech.sobhan.golestan.security.config.PasswordConfiguration.getPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = getPasswordEncoder();
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
//                .httpBasic().and()
//                .authorizeRequests()
//                .antMatchers("/instructors/").authenticated()
//                .antMatchers("/management/**").authenticated()
//                .antMatchers("/management/**").hasRole(ADMIN.name())
//                .antMatchers(MODIFY_ROLE_PATH).authenticated()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login")
//                    .permitAll()
//                    .usernameParameter("username")
//                    .passwordParameter("password")
//                .and()
//                .rememberMe()
//                    .tokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30))
//                    .key("one_fellow_over_the_cuckoos_nest")
//                    .rememberMeParameter("remember-me")
//                .and()
//                .logout()
//                    .logoutUrl("/logout")
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                    .clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID", "remember-me")
//                    .logoutSuccessUrl("/login");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth){
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

//    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }
}
