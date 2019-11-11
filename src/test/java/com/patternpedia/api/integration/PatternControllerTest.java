package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @Test
    public void addPatternToPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguage();

        Pattern pattern = this.integrationTestHelper.getDefaultPattern();

        MvcResult postResult = this.mockMvc.perform(
                post("/patternLanguages/{id}/patterns", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(pattern))
        ).andExpect(status().isCreated())
                .andReturn();

        String patternLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(patternLocation)
        ).andExpect(status().isOk());
    }

    @Test
    public void findPatternById() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPatterns(1);

        MvcResult getResult = this.mockMvc.perform(
                get("/patternLanguages/{plId}/patterns/{pId}",
                        patternLanguage.getId().toString(),
                        patternLanguage.getPatterns().get(0).getId().toString())
        ).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deletePatternFromPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPatterns(2);

        Pattern pattern = patternLanguage.getPatterns().get(0);

        this.mockMvc.perform(
                delete("/patternLanguages/{plId}/patterns/{pId}", patternLanguage.getId(), pattern.getId())
        ).andExpect(status().isOk());

        patternLanguage = this.patternLanguageRepository.getOne(patternLanguage.getId());
        assertThat(patternLanguage.getPatterns()).hasSize(1);
    }

}
