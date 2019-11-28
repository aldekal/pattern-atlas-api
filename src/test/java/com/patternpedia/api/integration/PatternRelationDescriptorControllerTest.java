package com.patternpedia.api.integration;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.rest.model.CreateDirectedEdgeRequest;
import com.patternpedia.api.rest.model.CreateUndirectedEdgeRequest;
import com.patternpedia.api.util.IntegrationTestHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternRelationDescriptorControllerTest {

    @Autowired
    IntegrationTestHelper integrationTestHelper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void cleanUpReposBefore() {
        this.integrationTestHelper.cleanUpRepos();
    }

    @Test
    public void addDirectedEdgeToPatternLanguageSucceeds() throws Exception {

        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        CreateDirectedEdgeRequest request = CreateDirectedEdgeRequest.builder(
                patternLanguage.getPatterns().get(0).getId(),
                patternLanguage.getPatterns().get(1).getId()
        ).build();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages/{patternLanguageId}/directedEdges", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

    @Test
    public void addUndirectedEdgeSucceedsToPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        CreateUndirectedEdgeRequest request = CreateUndirectedEdgeRequest.builder(
                patternLanguage.getPatterns().get(0).getId(),
                patternLanguage.getPatterns().get(1).getId()
        ).build();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages/{patternLanguageId}/undirectedEdges", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }
}
