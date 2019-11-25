package com.patternpedia.api.integration;

import java.util.UUID;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import com.patternpedia.api.repositories.PatternSchemaRepository;
import com.patternpedia.api.repositories.PatternSectionSchemaRepository;
import com.patternpedia.api.repositories.PatternViewRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;
import com.patternpedia.api.util.IntegrationTestHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private PatternViewRepository patternViewRepository;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private DirectedEdgeRepository directedEdgeRepository;

    @Autowired
    private UndirectedEdgeReository undirectedEdgeReository;

    @Autowired
    private PatternSchemaRepository patternSchemaRepository;

    @Autowired
    private PatternSectionSchemaRepository patternSectionSchemaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @After
    public void cleanUpRepos() {
        this.integrationTestHelper.cleanUpRepos();
    }

    @Test
    public void createPatternLanguageWithoutSchemaFails() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithoutSchema();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("No PatternSchema defined!");
    }

    @Test
    public void createPatternLanguageWithSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithSchema();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        MvcResult result1 = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        PatternLanguage patternLanguage1 = this.objectMapper.readValue(result1.getResponse().getContentAsString(), PatternLanguage.class);

        assertThat(patternLanguage1.getPatternSchema().getPatternSectionSchemas()).hasSize(2);
    }

    @Test
    public void updatePatternLanguageSucceeds() throws Exception {
        PatternLanguage createdPatternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        String newName = "TestPatternLanguage - Updated";
        createdPatternLanguage.setName(newName);

        MvcResult result = this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", createdPatternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(createdPatternLanguage))
        ).andExpect(status().isOk()).andReturn();

        PatternLanguage updatedPatternLanguage = this.objectMapper.readValue(result.getResponse().getContentAsByteArray(), PatternLanguage.class);

        assertThat(updatedPatternLanguage.getName()).isEqualTo(newName);
    }

    @Test
    public void createPatternLanguageViaPutFails() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithSchema();
        patternLanguage.setId(UUID.randomUUID());

        this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void deletePatternLanguageSucceeds() throws Exception {
        PatternLanguage createdPatternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        this.mockMvc.perform(
                delete("/patternLanguages/{patternLanguageId}", createdPatternLanguage.getId())
        ).andExpect(status().isNoContent());
    }
}
