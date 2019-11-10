package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.util.IntegrationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;


    @Test
    @Transactional
    @Commit
    public void addDirectedEdgeSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPatterns(2);

        DirectedEdge directedEdge = new DirectedEdge();
        directedEdge.setPatternLanguage(patternLanguage);
        directedEdge.setSource(patternLanguage.getPatterns().get(0));
        directedEdge.setTarget(patternLanguage.getPatterns().get(1));

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages/{patternLanguageId}/directedEdges", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(directedEdge))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Commit
    public void addUndirectedEdgeSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPatterns(2);

        UndirectedEdge undirectedEdge = new UndirectedEdge();
        undirectedEdge.setPatternLanguage(patternLanguage);
        undirectedEdge.setP1(patternLanguage.getPatterns().get(0));
        undirectedEdge.setP2(patternLanguage.getPatterns().get(1));

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages/{patternLanguageId}/undirectedEdges", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(undirectedEdge))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

}
