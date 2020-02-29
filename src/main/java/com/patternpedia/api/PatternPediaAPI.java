package com.patternpedia.api;

import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.pattern.evolution.PatternEvolution;
import com.patternpedia.api.service.PatternEvolutionService;
import com.patternpedia.api.service.UserService;
import com.vladmihalcea.hibernate.type.util.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebMvc
@Slf4j
public class PatternPediaAPI implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PatternEvolutionService patternEvolutionService;

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternPediaAPI.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("PatternPediaAPI is up");

        UserEntity user = new UserEntity();
        UserEntity u = userService.createUser(user);
        log.info(u.toString());

        PatternEvolution patternEvolution = new PatternEvolution();
        patternEvolution.setUri("uri");
        patternEvolution.setName("name");
        PatternEvolution p = patternEvolutionService.createPatternEvolution(patternEvolution);
        log.info(p.toString());
    }

   /* @Configuration
    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt();
        }
    }*/
}
