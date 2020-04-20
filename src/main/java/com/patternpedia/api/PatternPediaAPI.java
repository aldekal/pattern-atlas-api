package com.patternpedia.api;

import com.patternpedia.api.entities.evolution.PatternEvolution;
//import com.patternpedia.api.rest.controller.UserController;
import com.patternpedia.api.rest.controller.UserController;
import com.patternpedia.api.service.PatternEvolutionService;
import com.vladmihalcea.hibernate.type.util.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
//@EnableWebMvc
//@EnableTransactionManagement
//@EnableResourceServer
@RestController
@SpringBootApplication
public class PatternPediaAPI implements CommandLineRunner {

    @Autowired
    private UserController userController;

    @Autowired
    private PatternEvolutionService patternEvolutionService;

//    @GetMapping("/test")
//    @PreAuthorize("#oauth2.hasScope('delete')")
//    public Map<String,Object> home() {
//        Map<String,Object> model = new HashMap<String,Object>();
//        model.put("id", UUID.randomUUID().toString());
//        model.put("content", "Hello test");
//        return model;
//    }
//
//    @GetMapping("/home")
//    @PreAuthorize("#oauth2.hasScope('read')")
//    public Map<String,Object> test() {
//        Map<String,Object> model = new HashMap<String,Object>();
//        model.put("id", UUID.randomUUID().toString());
//        model.put("content", "Hello home");
//        return model;
//    }

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternPediaAPI.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("PatternPediaAPI is up");
        userController.defaultUser();
//        UserEntity u = userController.newUser("Paul", "a@a", "pass");
//        log.info(u.toString());

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
