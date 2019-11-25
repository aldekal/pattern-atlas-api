package com.patternpedia.api.integration;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;
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
public class PatternViewTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @After
    public void cleanUpRepos() {
        this.integrationTestHelper.cleanUpRepos();
    }

    @Test
    public void createPatternViewSucceeds() throws Exception {
        PatternView patternView = this.integrationTestHelper.getDefaultPatternView();

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternView))
        ).andExpect(status().isCreated()).andReturn();

        String location = postResult.getResponse().getHeader("Location");

        MvcResult getResult = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        PatternView createdPatternView = this.objectMapper.readValue(getResult.getResponse().getContentAsByteArray(), PatternView.class);
        assertThat(createdPatternView.getId()).isNotNull();
        assertThat(createdPatternView.getName()).isEqualTo(patternView.getName());
        assertThat(createdPatternView.getUri()).isEqualTo(patternView.getUri());
    }

    @Test
    public void addPatternsToPatternViewSucceeds() throws Exception {
        int numberOfPatterns = 5;
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(numberOfPatterns);
        PatternView patternView = this.integrationTestHelper.setUpPatternView();

        for (Pattern pattern : patternLanguage.getPatterns()) {
            MvcResult postResponse = this.mockMvc.perform(
                    post("/patternViews/{viewId}/patterns", patternView.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(pattern))
            ).andExpect(status().isCreated()).andReturn();

            String location = postResponse.getResponse().getHeader("Location");

            this.mockMvc.perform(
                    get(location)
            ).andExpect(status().isOk()).andReturn();
        }

        MvcResult getPatternViewResponse = this.mockMvc.perform(
                get("/patternViews/{viewId}", patternView.getId())
        ).andExpect(status().isOk()).andReturn();

        PatternView updatedPatternView = this.objectMapper.readValue(getPatternViewResponse.getResponse().getContentAsByteArray(), PatternView.class);

        assertThat(updatedPatternView.getPatterns()).isNotNull();
        assertThat(updatedPatternView.getPatterns()).hasSize(numberOfPatterns);
    }
}
