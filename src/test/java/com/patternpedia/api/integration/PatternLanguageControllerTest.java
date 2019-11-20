package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.PatternSectionSchema;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternService;
import com.patternpedia.api.util.IntegrationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternLanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @Autowired
    private PatternLanguageService patternLanguageService;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternService patternService;

    @Autowired
    private DirectedEdgeRepository directedEdgeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Autowired
    private PatternSchemaRepository patternSchemaRepository;

    @Autowired
    private PatternSectionSchemaRepository patternSectionSchemaRepository;

    @Test
    public void addPatternLanguageFailsDueToMissingSchema() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage5");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage5");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("No PatternSchema defined!");
    }

    @Test
    public void addPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage5");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage5");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        PatternSchema patternSchema = new PatternSchema();
        PatternSectionSchema patternSectionSchema = new PatternSectionSchema();
        patternSectionSchema.setPosition(0);
        patternSectionSchema.setName("test");
        patternSectionSchema.setLabel("test");
        patternSectionSchema.setType("test");
        patternSectionSchema.setPatternSchema(patternSchema);
        patternSchema.setPatternSectionSchemas(new ArrayList<>(Collections.singletonList(patternSectionSchema)));

        patternLanguage.setPatternSchema(patternSchema);

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

    @Test
    public void updatePatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguage();

        patternLanguage.setName("TestPatternLanguage6 - Updated");

        ObjectNode expectedUpdate = this.objectMapper.createObjectNode();
        expectedUpdate.put("name", "TestPatternLanguage6 - Updated");

        MvcResult result = this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedUpdate)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void createPatternLanguageViaPutFails() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("PutTestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/PutTestPatternLanguage");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));
        patternLanguage.setId(UUID.randomUUID());

        this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void createPatternLanguageWithPatternSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage1");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage1");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        PatternSchema patternSchema = new PatternSchema();

        PatternSectionSchema patternSectionSchema = new PatternSectionSchema();
        patternSectionSchema.setPosition(1);
        patternSectionSchema.setType("TestType");
        patternSectionSchema.setName("TestName");
        patternSectionSchema.setLabel("TestLabel");
        patternSectionSchema.setPatternSchema(patternSchema);

        List<PatternSectionSchema> patternSectionSchemas = new ArrayList<>();
        patternSectionSchemas.add(patternSectionSchema);

        patternSchema.setPatternSectionSchemas(patternSectionSchemas);

        patternLanguage.setPatternSchema(patternSchema);

        MvcResult result = this.mockMvc.perform(post("/patternLanguages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(patternLanguage)))
                .andExpect(status().isCreated())
                .andReturn();

        // Todo: Rewrite test -> check the content of MvcResult
        assertThat(patternLanguage).hasFieldOrPropertyWithValue("name", "TestPatternLanguage1");
        assertThat(patternLanguage.getPatternSchema()).isNotNull();
        assertThat(patternLanguage.getPatternSchema().getPatternSectionSchemas()).isNotNull();
        assertThat(patternLanguage.getPatternSchema().getPatternSectionSchemas().size()).isEqualTo(1);
        assertThat(patternLanguage.getPatternSchema().getPatternSectionSchemas().get(0)).isEqualToComparingOnlyGivenFields(patternSectionSchema, "type", "name", "label", "position");

    }

    @Test
    public void getAllPatternsFromPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPatterns(2);

        Pattern p1 = patternLanguage.getPatterns().get(0);
        Pattern p2 = patternLanguage.getPatterns().get(1);

        String expectedContent = String.format("{\"_embedded\":{\"patterns\":[{\"id\": \"%s\"},{\"id\": \"%s\"}]}}", p1.getId(), p2.getId());
        JsonNode expectedResult = this.objectMapper.readTree(expectedContent);

        this.mockMvc.perform(
                get("/patternLanguages/{id}/patterns", patternLanguage.getId())
        ).andExpect(status().isOk())
                .andExpect(content().json(expectedResult.toString()));
    }

    @Test
    public void createAndGetPatternSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguage();

        PatternSchema patternSchema = new PatternSchema();
        patternSchema.setPatternLanguage(patternLanguage);

        PatternSectionSchema patternSectionSchema = new PatternSectionSchema();
        patternSectionSchema.setPatternSchema(patternSchema);
        patternSectionSchema.setLabel("Test");
        patternSectionSchema.setName("Test");
        patternSectionSchema.setPosition(0);
        patternSchema.setPatternSectionSchemas(new ArrayList<>(Collections.singletonList(patternSectionSchema)));

        MvcResult postResult = this.mockMvc.perform(
                post("/patternLanguages/{id}/patternSchema", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternSchema)))
                .andExpect(status().isCreated()).andReturn();

        String location = postResult.getResponse().getHeader("Location");

        MvcResult result = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        PatternSchema patternSchemaResult = this.objectMapper.readValue(result.getResponse().getContentAsString(), PatternSchema.class);
        assertThat(patternSchemaResult.getPatternSectionSchemas()).hasSize(1);
        assertThat(patternSchemaResult.getPatternSectionSchemas().get(0)).hasFieldOrPropertyWithValue("label", "Test");
        assertThat(patternSchemaResult.getPatternSectionSchemas().get(0)).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(patternSchemaResult.getPatternSectionSchemas().get(0)).hasFieldOrPropertyWithValue("position", 0);
    }
}
