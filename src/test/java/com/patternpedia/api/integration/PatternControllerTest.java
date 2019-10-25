package com.patternpedia.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.repositories.PatternRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    public void testPatternRepositoryCreateSucceeds() {
        Pattern pattern = new Pattern();
        pattern.setUri("http://patternpedia.org/testPatterns/TestPattern1");
        pattern.setName("TestPattern1");

        ObjectNode content = this.objectMapper.createObjectNode();

        content.put("Field1", "FieldValue1");
        content.put("Field2", 123);

        pattern.setContent(content);

        this.patternRepository.save(pattern);
    }

    @Test
    public void testGetPatternById() throws Exception {
        Pattern pattern = new Pattern();
        pattern.setUri("http://patternpedia.org/testPatterns/TestPattern2");
        pattern.setName("TestPattern2");

        ObjectNode content = this.objectMapper.createObjectNode();
        content.put("Field1", "FieldValue1");
        content.put("Field2", 123);

        pattern.setContent(content);

        Pattern managedPattern = this.patternRepository.save(pattern);

        ObjectNode expectedJson = this.objectMapper.valueToTree(managedPattern);
        expectedJson.remove("id");

        MvcResult getResult = this.mockMvc.perform(
                get("/patterns/{id}", managedPattern.getId().toString())
        ).andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedJson)))
                .andReturn();
    }


}
