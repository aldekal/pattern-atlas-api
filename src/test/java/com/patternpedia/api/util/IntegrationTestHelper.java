package com.patternpedia.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.PatternSectionSchema;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import com.patternpedia.api.repositories.PatternSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IntegrationTestHelper {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @Autowired
    private PatternSchemaRepository patternSchemaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Pattern createOrGetPattern(Pattern p) {
        if (null != p.getUri() && this.patternRepository.existsByUri(p.getUri())) {
            return this.patternRepository.findByUri(p.getUri()).get();
        } else {
            if (null == p.getContent()) {
                p.setContent(this.getDefaultPatternContent());
            }
            return this.patternRepository.save(p);
        }
    }

    public ObjectNode getDefaultPatternContent() {
        ObjectNode content = this.objectMapper.createObjectNode();
        content.put("Field1", "FieldValue1");
        content.put("Field2", 123);
        return content;
    }

    public Pattern getDefaultPattern() {
        Pattern pattern = this.getUnpersistedDefaultPattern();
        return this.createOrGetPattern(pattern);
    }

    public Pattern getUnpersistedDefaultPattern() {
        Pattern pattern = new Pattern();
        pattern.setUri("http://patternpedia.org/testPatterns/TestPattern1");
        pattern.setName("TestPattern1");

        ObjectNode content = this.getDefaultPatternContent();
        pattern.setContent(content);
        return pattern;
    }

    public PatternLanguage createOrGetPatternLanguage(PatternLanguage patternLanguage) {
        if (null != patternLanguage.getUri() && this.patternLanguageRepository.existsByUri(patternLanguage.getUri())) {
            return this.patternLanguageRepository.findByUri(patternLanguage.getUri()).get();
        } else {
            patternLanguage = this.patternLanguageRepository.save(patternLanguage);
            return patternLanguage;
        }
    }

    public PatternLanguage getDefaultPatternLanguage() {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("DefaultTestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/patternlanguages/DefaultTestPatternLanguage");
        patternLanguage.setPatternSchema(this.getUnpersistedDefaultPatternSchema());
        return this.createOrGetPatternLanguage(patternLanguage);
    }

    public PatternSchema getUnpersistedDefaultPatternSchema() {
        PatternSchema patternSchema = new PatternSchema();

        PatternSectionSchema patternSectionSchema1 = new PatternSectionSchema();
        patternSectionSchema1.setName("Field1");
        patternSectionSchema1.setLabel("Field1");
        patternSectionSchema1.setPosition(0);
        patternSectionSchema1.setType("string");
        patternSectionSchema1.setPatternSchema(patternSchema);

        PatternSectionSchema patternSectionSchema2 = new PatternSectionSchema();
        patternSectionSchema2.setName("Field2");
        patternSectionSchema2.setLabel("Field2");
        patternSectionSchema2.setPosition(0);
        patternSectionSchema2.setType("number");
        patternSectionSchema2.setPatternSchema(patternSchema);

        List<PatternSectionSchema> patternSectionSchemas = new ArrayList<>();
        patternSectionSchemas.add(patternSectionSchema1);
        patternSectionSchemas.add(patternSectionSchema2);
        patternSchema.setPatternSectionSchemas(patternSectionSchemas);
        return patternSchema;
    }

    public PatternLanguage getDefaultPatternLanguageWithPatterns(int numberOfPatterns) {
        PatternLanguage patternLanguage = this.getDefaultPatternLanguage();
        if (numberOfPatterns > 0) {
            for (int i = 0; i < numberOfPatterns; i++) {
                Pattern pattern = new Pattern();
                pattern.setUri("https://patternpedia.org/patterns/TestPattern" + (i + 1));
                pattern.setName("TestPattern" + (i + 1));
                pattern.setPatternLanguage(patternLanguage);

                pattern = this.createOrGetPattern(pattern);

                if (null != patternLanguage.getPatterns()) {
                    patternLanguage.getPatterns().add(pattern);
                } else {
                    patternLanguage.setPatterns(new ArrayList<>(Collections.singletonList(pattern)));
                }
            }
            patternLanguage = this.patternLanguageRepository.save(patternLanguage);
        }
        return patternLanguage;
    }

}
