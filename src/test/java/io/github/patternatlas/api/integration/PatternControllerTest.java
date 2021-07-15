package io.github.patternatlas.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.util.IntegrationTestHelper;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternLanguage;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Before
    public void cleanUpReposBefore() {
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

        MvcResult getPatternLanguageResponse = this.mockMvc.perform(
                get("/patternLanguages/{plId}", patternLanguage.getId())
        ).andExpect(status().isOk()).andReturn();

        JsonNode response = this.objectMapper.readTree(getPatternLanguageResponse.getResponse().getContentAsByteArray());

        String patternsLocation = response.get("_links").get("patterns").get("href").textValue();

        MvcResult getPatternsResponse = this.mockMvc.perform(
                get(patternsLocation)
        ).andExpect(status().isOk()).andReturn();

        JsonNode getPatternsResult = this.objectMapper.readTree(getPatternsResponse.getResponse().getContentAsByteArray());

        List<Pattern> patterns = this.objectMapper.readValue(
                getPatternsResult.get("_embedded").get("patternModels").toString(),
                this.objectMapper.getTypeFactory().constructCollectionType(List.class, Pattern.class)
        );

        assertThat(patterns).hasSize(1);
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
