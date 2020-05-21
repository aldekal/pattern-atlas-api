package com.patternpedia.api;

import com.patternpedia.api.entities.issue.Issue;
//import com.patternpedia.api.rest.controller.UserController;
import com.patternpedia.api.rest.controller.UserController;
import com.patternpedia.api.service.IssueService;
import com.vladmihalcea.hibernate.type.util.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableTransactionManagement
@EnableWebMvc
@Slf4j
@RestController
@SpringBootApplication
public class PatternPediaAPI implements CommandLineRunner {

    @Autowired
    private UserController userController;

    @Autowired
    private IssueService issueService;

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternPediaAPI.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("PatternPediaAPI is up");
    }
}
