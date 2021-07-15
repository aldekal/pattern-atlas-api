package io.github.patternatlas.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

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

import io.github.patternatlas.api.util.IntegrationTestHelper;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternSchema;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatternLanguageControllerTest extends IntegrationTest {

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
    public void createPatternLanguageWithoutSchemaFails() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithoutSchema();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("No PatternSchema defined!");
    }

    @Test
    public void createPatternLanguageWithSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithSchema();

        MvcResult result = this.mockMvc.perform(
                post("/patternLanguages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        MvcResult result1 = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        PatternLanguage patternLanguage1 = this.objectMapper.readValue(result1.getResponse().getContentAsString(), PatternLanguage.class);

        assertThat(patternLanguage1.getPatternSchema().getPatternSectionSchemas()).hasSize(2);
    }

    @Test
    public void updatePatternLanguageSucceeds() throws Exception {
        PatternLanguage createdPatternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        String newName = "TestPatternLanguage - Updated";
        createdPatternLanguage.setName(newName);

        this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", createdPatternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(createdPatternLanguage))
        ).andExpect(status().isNoContent()).andReturn();

        MvcResult mvcResult = this.mockMvc.perform(
                get("/patternLanguages/{patternLanguageId}", createdPatternLanguage.getId())
        ).andExpect(status().isOk()).andReturn();

        PatternLanguage updatedPatternLanguage = this.objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), PatternLanguage.class);

        assertThat(updatedPatternLanguage.getName()).isEqualTo(newName);
    }

    @Test
    public void createPatternLanguageViaPutFails() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.getDefaultPatternLanguageWithSchema();
        patternLanguage.setId(UUID.randomUUID());

        this.mockMvc.perform(
                put("/patternLanguages/{patternLanguageId}", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternLanguage))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void deletePatternLanguageSucceeds() throws Exception {
        PatternLanguage createdPatternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        this.mockMvc.perform(
                delete("/patternLanguages/{patternLanguageId}", createdPatternLanguage.getId())
        ).andExpect(status().isNoContent());
    }

    @Test
    public void updatePatternSchemaSucceeds() throws Exception {
        PatternLanguage patternLanguage = this.integrationTestHelper.setUpPatternLanguage(0);

        MvcResult getPatternSchemaResponse = this.mockMvc.perform(
                get("/patternLanguages/{plId}/patternSchema", patternLanguage.getId())
        ).andExpect(status().isOk()).andReturn();

        PatternSchema patternSchema = this.objectMapper.readValue(getPatternSchemaResponse.getResponse().getContentAsByteArray(), PatternSchema.class);

        patternSchema.getPatternSectionSchemas().remove(1);

        this.mockMvc.perform(
                put("/patternLanguages/{plId}/patternSchema", patternLanguage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternSchema))
        ).andExpect(status().isNoContent()).andReturn();

        getPatternSchemaResponse = this.mockMvc.perform(
                get("/patternLanguages/{plId}/patternSchema", patternLanguage.getId())
        ).andExpect(status().isOk()).andReturn();

        patternSchema = this.objectMapper.readValue(getPatternSchemaResponse.getResponse().getContentAsByteArray(), PatternSchema.class);

        assertThat(patternSchema.getPatternSectionSchemas()).hasSize(1);
    }
}
