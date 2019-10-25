package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatternServiceTest {

    @Autowired
    private PatternService patternService;

    @Autowired
    private PatternLanguageService patternLanguageService;

    @Test
    public void testPatternValidator() {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/TestPatternLanguage");

        PatternLanguage saved = this.patternLanguageService.createPatternLanguage(patternLanguage);

        Pattern p = new Pattern();
        p.setName("TestPattern");
        p.setUri("http://patternpedia.org/TestPatternLanguage/TestPattern");
        p.setPatternLanguage(saved);

        this.patternService.createPattern(p);

    }
}
