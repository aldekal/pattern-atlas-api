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
import io.github.patternatlas.api.entities.PatternView;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternViewTest extends IntegrationTest {
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

        JsonNode node = this.objectMapper.readTree(getPatternViewResponse.getResponse().getContentAsByteArray());
        String linkToPatterns = node.get("_links").get("patterns").get("href").textValue();

        MvcResult getPatternViewPatternsResponse = this.mockMvc.perform(
                get(linkToPatterns)
        ).andExpect(status().isOk()).andReturn();

        JsonNode response = this.objectMapper.readTree(getPatternViewPatternsResponse.getResponse().getContentAsByteArray());

        List<Pattern> patterns = this.objectMapper.readValue(
                response.get("_embedded").get("patternModels").toString(),
                this.objectMapper.getTypeFactory().constructCollectionType(List.class, Pattern.class)
        );

        assertThat(patterns).hasSize(numberOfPatterns);
    }
}
