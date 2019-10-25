package com.patternpedia.api.service;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.PatternSectionSchema;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.util.IntegrationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatternSchemaServiceTest {

    @Autowired
    private PatternSchemaService patternSchemaService;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Test
    public void createGivenSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/test/patternLanguages/TestPatternLanguage");
        PatternLanguage savedPatternLanguage = integrationTestHelper.createOrGetPatternLanguage(patternLanguage);

        PatternSchema patternSchema = new PatternSchema();
        patternSchema.setPatternLanguage(savedPatternLanguage);

        PatternSectionSchema patternSectionSchema1 = new PatternSectionSchema();
        patternSectionSchema1.setPatternSchema(patternSchema);
        patternSectionSchema1.setLabel("TestPatternSectionSchema1Label");
        patternSectionSchema1.setName("TestPatternSectionSchema1Name");
        patternSectionSchema1.setType("TestPatternSectionSchema1Type");
        patternSectionSchema1.setPosition(1);

        PatternSectionSchema patternSectionSchema2 = new PatternSectionSchema();
        patternSectionSchema2.setPatternSchema(patternSchema);
        patternSectionSchema2.setLabel("TestPatternSectionSchema2Label");
        patternSectionSchema2.setName("TestPatternSectionSchema2Name");
        patternSectionSchema2.setType("TestPatternSectionSchema2Type");
        patternSectionSchema2.setPosition(2);

        List<PatternSectionSchema> patternSectionSchemas = new ArrayList<>();
        patternSectionSchemas.add(patternSectionSchema1);
        patternSectionSchemas.add(patternSectionSchema2);
        patternSchema.setPatternSectionSchemas(patternSectionSchemas);

        PatternSchema savedSchema = this.patternSchemaService.createPatternSchema(patternSchema);

        assertThat(savedSchema.getPatternSectionSchemas()).isNotNull();
        assertThat(savedSchema.getPatternSectionSchemas().size()).isEqualTo(2);

    }

    @Test
    public void updateGivenSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/test/patternLanguages/TestPatternLanguage");
        patternLanguage = integrationTestHelper.createOrGetPatternLanguage(patternLanguage);

        PatternSchema patternSchema = new PatternSchema();
        patternSchema.setPatternLanguage(patternLanguage);

        PatternSectionSchema patternSectionSchema1 = new PatternSectionSchema();
        patternSectionSchema1.setPatternSchema(patternSchema);
        patternSectionSchema1.setLabel("TestPatternSectionSchema1Label");
        patternSectionSchema1.setName("TestPatternSectionSchema1Name");
        patternSectionSchema1.setType("TestPatternSectionSchema1Type");
        patternSectionSchema1.setPosition(1);

        PatternSectionSchema patternSectionSchema2 = new PatternSectionSchema();
        patternSectionSchema2.setPatternSchema(patternSchema);
        patternSectionSchema2.setLabel("TestPatternSectionSchema2Label");
        patternSectionSchema2.setName("TestPatternSectionSchema2Name");
        patternSectionSchema2.setType("TestPatternSectionSchema2Type");
        patternSectionSchema2.setPosition(2);

        List<PatternSectionSchema> patternSectionSchemas = new ArrayList<>();
        patternSectionSchemas.add(patternSectionSchema1);
        patternSectionSchemas.add(patternSectionSchema2);
        patternSchema.setPatternSectionSchemas(patternSectionSchemas);

        patternSchema = this.patternSchemaService.createPatternSchema(patternSchema);

        assertThat(patternSchema.getPatternSectionSchemas()).isNotNull();
        assertThat(patternSchema.getPatternSectionSchemas().size()).isEqualTo(2);

        PatternSectionSchema patternSectionSchema3 = new PatternSectionSchema();
        patternSectionSchema3.setPatternSchema(patternSchema);
        patternSectionSchema3.setLabel("TestPatternSectionSchema3Label");
        patternSectionSchema3.setName("TestPatternSectionSchema3Name");
        patternSectionSchema3.setType("TestPatternSectionSchema3Type");
        patternSectionSchema3.setPosition(2);

        patternSchema.getPatternSectionSchemas().remove(0);
        patternSchema.getPatternSectionSchemas().get(0).setPosition(1);
        patternSectionSchemas.add(patternSectionSchema3);

        patternSchema.setPatternSectionSchemas(patternSectionSchemas);

        this.patternSchemaService.updatePatternSchema(patternSchema);

        assertThat(patternSchema.getPatternSectionSchemas()).isNotNull();
        assertThat(patternSchema.getPatternSectionSchemas().size()).isEqualTo(2);
        PatternSectionSchema expectedSectionSchema = new PatternSectionSchema();
        expectedSectionSchema.setPosition(1);
        assertThat(patternSchema.getPatternSectionSchemas().get(0)).isEqualToComparingOnlyGivenFields(expectedSectionSchema, "position");
        assertThat(patternSchema.getPatternSectionSchemas().get(1)).isEqualToIgnoringGivenFields(patternSectionSchema3, "id");

    }
}
