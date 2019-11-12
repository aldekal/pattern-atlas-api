package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        Pattern pattern = this.integrationTestHelper.getUnpersistedDefaultPattern();

        String postContent = this.objectMapper.writerWithView(Pattern.PatternWithContent.class).writeValueAsString(pattern);

        MvcResult postResult = this.mockMvc.perform(
                post("/patternLanguages/{id}/patterns", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postContent)
        ).andExpect(status().isCreated())
                .andReturn();

        String patternLocation = postResult.getResponse().getHeader("Location");
        MvcResult getResult = this.mockMvc.perform(
                get(patternLocation)
        ).andExpect(status().isOk()).andReturn();

        JsonNode patternJson = this.objectMapper.readTree(getResult.getResponse().getContentAsByteArray());
        String contentUri = patternJson.get("_links").get("content").get("href").textValue();

        MvcResult patternContentMvcResult = this.mockMvc.perform(
                get(contentUri)
        ).andExpect(status().isOk())
                .andReturn();

        JsonNode result = this.objectMapper.readTree(patternContentMvcResult.getResponse().getContentAsByteArray());
        assertThat(result.get("content")).isNotNull();
        assertThat(result.get("content").toString()).isEqualTo(this.integrationTestHelper.getDefaultPatternContent().toString());
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
