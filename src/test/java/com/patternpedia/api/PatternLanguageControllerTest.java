package com.patternpedia.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private PatternRepository patternRepository;

    @Autowired
    private DirectedEdgeRepository directedEdgeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddPatternToPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage1");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage1");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));
        this.patternLanguageRepository.save(patternLanguage);

        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/testPatterns/TestPattern1");
        p1.setName("TestPattern1");

        this.patternRepository.save(p1);

        MvcResult postResult = this.mockMvc.perform(
                post("/patternLanguages/{id}/patterns", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(p1))
        ).andExpect(status().isCreated())
                .andReturn();

        String patternLocation = postResult.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(patternLocation)
        ).andExpect(status().isOk());
    }

    @Test
    public void testAddDirectedEdgeViaRepositorySucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage2");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage2");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));
        this.patternLanguageRepository.save(patternLanguage);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/testPatterns/TestPattern2");
        p2.setName("TestPattern2");

        Pattern p3 = new Pattern();
        p3.setUri("http://patternpedia.org/testPatterns/TestPattern3");
        p3.setName("TestPattern3");

        this.patternRepository.save(p2);
        this.patternRepository.save(p3);

        DirectedEdge directedEdge = new DirectedEdge();
        directedEdge.setPatternLanguage(patternLanguage);
        directedEdge.setSource(p2);
        directedEdge.setTarget(p3);

        this.directedEdgeRepository.save(directedEdge);
        List<DirectedEdge> directedEdges = new ArrayList<>();
        directedEdges.add(directedEdge);

        patternLanguage.setDirectedEdges(directedEdges);

        this.patternLanguageRepository.save(patternLanguage);
    }

    @Test
    public void testAddDirectedEdgeSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage3");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage3");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));
        this.patternLanguageRepository.save(patternLanguage);

        Pattern p4 = new Pattern();
        p4.setUri("http://patternpedia.org/testPatterns/TestPattern4");
        p4.setName("TestPattern4");

        Pattern p5 = new Pattern();
        p5.setUri("http://patternpedia.org/testPatterns/TestPattern5");
        p5.setName("TestPattern5");

        this.patternRepository.save(p4);
        this.patternRepository.save(p5);

        DirectedEdge directedEdge = new DirectedEdge();
        directedEdge.setPatternLanguage(patternLanguage);
        directedEdge.setSource(p4);
        directedEdge.setTarget(p5);

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
    public void testAddUndirectedEdgeSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage4");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage4");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));
        this.patternLanguageRepository.save(patternLanguage);

        Pattern p6 = new Pattern();
        p6.setUri("http://patternpedia.org/testPatterns/TestPattern6");
        p6.setName("TestPattern6");

        Pattern p7 = new Pattern();
        p7.setUri("http://patternpedia.org/testPatterns/TestPattern7");
        p7.setName("TestPattern7");

        this.patternRepository.save(p6);
        this.patternRepository.save(p7);

        UndirectedEdge undirectedEdge = new UndirectedEdge();
        undirectedEdge.setPatternLanguage(patternLanguage);
        undirectedEdge.setP1(p6);
        undirectedEdge.setP2(p7);

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

    @Test
    public void testAddPatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage5");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage5");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdatePatternLanguageSucceeds() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage6");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage6");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        this.patternLanguageRepository.save(patternLanguage);

        patternLanguage.setName("TestPatternLanguage6 - Updated");

        ObjectNode expectedUpdate = this.objectMapper.createObjectNode();
        expectedUpdate.put("name", "TestPatternLanguage6 - Updated");

        MvcResult result = this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedUpdate)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testCreatePatternLanguageViaPutFailure() throws Exception {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("TestPatternLanguage7");
        patternLanguage.setUri("http://patternpedia.org/testPatternLanguages/TestPatternLanguage7");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        patternLanguage.setId(UUID.randomUUID());

        this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError());
    }
}
