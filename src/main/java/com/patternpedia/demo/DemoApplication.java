package com.patternpedia.demo;

import com.patternpedia.demo.entities.*;
import com.patternpedia.demo.repositories.PatternGraphRepository;
import com.patternpedia.demo.repositories.PatternLanguageRepository;
import com.patternpedia.demo.repositories.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@SpringBootApplication
@EnableTransactionManagement
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternGraphRepository patternGraphRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        PatternLanguage pl = new PatternLanguage("http://patternpedia.org/greenitpatterns", "Green IT Patterns", new TreeSet<Pattern>(), new HashSet<PatternRelationDescriptor>(), new ArrayList<PatternSectionType>());
        patternGraphRepository.save(pl);
        List<PatternGraph> list = new ArrayList<PatternGraph>();
        patternGraphRepository.findAll().forEach(list::add);
        System.out.println(list.size());
        List<PatternLanguage> plList = new ArrayList<PatternLanguage>();
        patternLanguageRepository.findAll().forEach(plList::add);
        System.out.println(plList.size());
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
