package com.patternpedia.api.integration;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @After
    public void cleanUpRepos() {
        this.integrationTestHelper.cleanUpRepos();
    }

    @Test
    public void addPatternToPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        Pattern pattern = this.integrationTestHelper.getDefaultPattern();

        MvcResult postResult = this.mockMvc.perform(
                post("/patternLanguages/{id}/patterns", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(pattern))
        ).andExpect(status().isCreated())
                .andReturn();

        String patternLocation = postResult.getResponse().getHeader("Location");

        MvcResult getPatternResponse = this.mockMvc.perform(
                get(patternLocation)
        ).andExpect(status().isOk()).andReturn();

        Pattern constraint = this.integrationTestHelper.getDefaultPattern();
        Pattern createdPattern = this.objectMapper.readValue(getPatternResponse.getResponse().getContentAsByteArray(), Pattern.class);

        MvcResult getPatternLanguageResponse = this.mockMvc.perform(
                get("/patternLanguages/{plId}", patternLanguage.getId())
        ).andExpect(status().isOk()).andReturn();

        patternLanguage = this.objectMapper
                .readValue(getPatternLanguageResponse.getResponse().getContentAsByteArray(), PatternLanguage.class);

        assertThat(patternLanguage.getPatterns()).hasSize(1);
    }

    @Test
    public void findPatternById() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(1);

        MvcResult getResult = this.mockMvc.perform(
                get("/patternLanguages/{plId}/patterns/{pId}",
                        patternLanguage.getId().toString(),
                        patternLanguage.getPatterns().get(0).getId().toString())
        ).andExpect(status().isOk())
                .andReturn();
    }
}
