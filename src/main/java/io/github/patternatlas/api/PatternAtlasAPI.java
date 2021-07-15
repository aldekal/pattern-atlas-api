package io.github.patternatlas.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import com.vladmihalcea.hibernate.type.util.Configuration;

import lombok.extern.slf4j.Slf4j;

import io.github.patternatlas.api.rest.controller.UserController;
import io.github.patternatlas.api.service.IssueService;

@EnableTransactionManagement
@Slf4j
@RestController
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "pattern-atlas-api", version = "1.0", contact = @Contact(url = "https://github.com/PatternAtlas/pattern-atlas-api", name = "Pattern Atlas API")))
public class PatternAtlasAPI implements CommandLineRunner {

    @Autowired
    private UserController userController;

    @Autowired
    private IssueService issueService;

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternAtlasAPI.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("PatternAtlasAPI is up");
    }
}
