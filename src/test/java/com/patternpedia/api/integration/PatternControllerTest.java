package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
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
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Test
    public void createPatternSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguage();

        Pattern pattern = new Pattern();
        pattern.setUri("http://patternpedia.org/testPatterns/TestPattern3");
        pattern.setName("TestPattern3");
        pattern.setPatternLanguage(patternLanguage);

        ObjectNode content = this.objectMapper.createObjectNode();

        content.put("Field1", "FieldValue1");
        content.put("Field2", 123);

        pattern.setContent(content);

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages/{id}/patterns", patternLanguage.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(pattern))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk());
    }

    @Test
    public void findPatternById() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithPattern();

        MvcResult getResult = this.mockMvc.perform(
                get("/patternLanguages/{plId}/patterns/{pId}",
                        patternLanguage.getId().toString(),
                        patternLanguage.getPatterns().get(0).getId().toString())
        ).andExpect(status().isOk())
                .andReturn();
    }


}
