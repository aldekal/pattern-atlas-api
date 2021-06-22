package io.github.patternatlas.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.rest.model.AddDirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.AddUndirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.CreateDirectedEdgeRequest;
import io.github.patternatlas.api.rest.model.CreateUndirectedEdgeRequest;
import io.github.patternatlas.api.util.IntegrationTestHelper;
import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternView;
import io.github.patternatlas.api.entities.UndirectedEdge;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternRelationDescriptorControllerTest extends IntegrationTest {

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
    public void addExistingDirectedEdgeToPatternViewSucceeds() throws Exception {

        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        PatternView patternView = this.integrationTestHelper.setUpPatternView();

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

        MvcResult getResult = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        DirectedEdge model = this.objectMapper.readValue(getResult.getResponse().getContentAsByteArray(), DirectedEdge.class);

        AddDirectedEdgeToViewRequest addEdgeRequest = new AddDirectedEdgeToViewRequest();
        addEdgeRequest.setDirectedEdgeId(model.getId());

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews/{patternViewId}/directedEdges", patternView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(addEdgeRequest))
        ).andExpect(status().isCreated()).andReturn();

        String addedEdgeLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(addedEdgeLocation)
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void addNewDirectedEdgeToPatternViewSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        PatternView patternView = this.integrationTestHelper.setUpPatternView();

        AddDirectedEdgeToViewRequest request = new AddDirectedEdgeToViewRequest();
        request.setSourcePatternId(patternLanguage.getPatterns().get(0).getId());
        request.setTargetPatternId(patternLanguage.getPatterns().get(1).getId());
        request.setDescription("This is a test description");
        request.setType("This is a test type");
        request.setNewEdge(true);

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews/{patternViewId}/directedEdges", patternView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(request))
        ).andExpect(status().isCreated()).andReturn();

        String addedEdgeLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(addedEdgeLocation)
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void addUndirectedEdgeToPatternLanguageSucceeds() throws Exception {
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

    @Test
    public void addExistingUndirectedEdgeToPatternViewSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        PatternView patternView = this.integrationTestHelper.setUpPatternView();

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

        MvcResult getResult = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        UndirectedEdge edge = this.objectMapper.readValue(getResult.getResponse().getContentAsByteArray(), UndirectedEdge.class);

        AddUndirectedEdgeToViewRequest addEdgeRequest = new AddUndirectedEdgeToViewRequest();
        addEdgeRequest.setUndirectedEdgeId(edge.getId());

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews/{patternViewId}/undirectedEdges", patternView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(addEdgeRequest))
        ).andExpect(status().isCreated()).andReturn();

        String addedEdgeLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(addedEdgeLocation)
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void addNewUndirectedEdgeToPatternViewSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(2);

        PatternView patternView = this.integrationTestHelper.setUpPatternView();

        AddUndirectedEdgeToViewRequest request = new AddUndirectedEdgeToViewRequest();
        request.setPattern1Id(patternLanguage.getPatterns().get(0).getId());
        request.setPattern2Id(patternLanguage.getPatterns().get(1).getId());
        request.setDescription("This is a test description");
        request.setType("This is a test type");
        request.setNewEdge(true);

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews/{patternViewId}/undirectedEdges", patternView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(request))
        ).andExpect(status().isCreated()).andReturn();

        String addedEdgeLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(addedEdgeLocation)
        ).andExpect(status().isOk()).andReturn();
    }
}
