package com.patternpedia.api;

import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.pattern.evolution.PatternEvolution;
import com.patternpedia.api.rest.controller.UserController;
import com.patternpedia.api.service.PatternEvolutionService;
import com.patternpedia.api.service.UserService;
import com.vladmihalcea.hibernate.type.util.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
//@EnableWebMvc
@EnableTransactionManagement
@EnableResourceServer
@RestController
@SpringBootApplication
public class PatternPediaAPI implements CommandLineRunner {

    @Autowired
    private UserController userController;

    @Autowired
    private PatternEvolutionService patternEvolutionService;

    @GetMapping("/")
    public Map<String,Object> home() {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternPediaAPI.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("PatternPediaAPI is up");
        UserEntity u = userController.newUser("Paul", "a@a", "pass");
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
