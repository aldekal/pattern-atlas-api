package com.patternpedia.api;

import com.patternpedia.api.filestorage.FileStorageProperties;
import com.vladmihalcea.hibernate.type.util.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebMvc
@Slf4j
@EnableConfigurationProperties({FileStorageProperties.class})
public class PatternPediaAPI implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
        SpringApplication.run(PatternPediaAPI.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("PatternPediaAPI is up");
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
