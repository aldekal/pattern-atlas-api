package io.github.patternatlas.api.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.patternatlas.api.entities.DiscussionComment;
import io.github.patternatlas.api.entities.DiscussionTopic;
import io.github.patternatlas.api.entities.Image;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternSchema;
import io.github.patternatlas.api.entities.PatternSectionSchema;
import io.github.patternatlas.api.entities.PatternView;
import io.github.patternatlas.api.repositories.DirectedEdgeRepository;
import io.github.patternatlas.api.repositories.DiscussionCommentRepository;
import io.github.patternatlas.api.repositories.DiscussionTopicRepository;
import io.github.patternatlas.api.repositories.ImageRepository;
import io.github.patternatlas.api.repositories.PatternLanguageRepository;
import io.github.patternatlas.api.repositories.PatternRepository;
import io.github.patternatlas.api.repositories.PatternSchemaRepository;
import io.github.patternatlas.api.repositories.PatternSectionSchemaRepository;
import io.github.patternatlas.api.repositories.PatternViewDirectedEdgeRepository;
import io.github.patternatlas.api.repositories.PatternViewPatternRepository;
import io.github.patternatlas.api.repositories.PatternViewRepository;
import io.github.patternatlas.api.repositories.UndirectedEdgeReository;

@Service
public class IntegrationTestHelper {

    @Autowired
    PatternViewPatternRepository patternViewPatternRepository;
    @Autowired
    PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository;
    @Autowired
    private PatternRepository patternRepository;
    @Autowired
    private PatternLanguageRepository patternLanguageRepository;
    @Autowired
    private PatternSchemaRepository patternSchemaRepository;
    @Autowired
    private PatternSectionSchemaRepository patternSectionSchemaRepository;
    @Autowired
    private DirectedEdgeRepository directedEdgeRepository;
    @Autowired
    private UndirectedEdgeReository undirectedEdgeReository;
    @Autowired
    private PatternViewRepository patternViewRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private DiscussionCommentRepository discussionCommentRepository;
    @Autowired
    private DiscussionTopicRepository discussionTopicRepository;

    public void cleanUpRepos() {
        System.out.println("Cleaning up...");

        this.patternSectionSchemaRepository.deleteAll();
        System.out.println("Cleaned up patternSectionSchemaRepository");

        this.patternSchemaRepository.deleteAll();
        System.out.println("Cleaned up patternSchemaRepository");

        this.patternViewPatternRepository.deleteAll();
        System.out.println("Cleaned up patternViewPatternRepository");

        this.patternViewDirectedEdgeRepository.deleteAll();
        System.out.println("Cleaned up patternViewDirectedEdgeRepository");

        this.directedEdgeRepository.deleteAll();
        System.out.println("Cleaned up directedEdgeRepository");

        this.undirectedEdgeReository.deleteAll();
        System.out.println("Cleaned up undirectedEdgeReository");

        this.patternViewRepository.deleteAll();
        System.out.println("Cleaned up patternViewRepository");

        this.patternRepository.deleteAll();
        System.out.println("Cleaned up patternRepository");

        this.patternLanguageRepository.deleteAll();
        System.out.println("Cleaned up patternLanguageRepository");

        this.imageRepository.deleteAll();
        System.out.println("Cleaned up imageRepository");

        this.discussionCommentRepository.deleteAll();
        System.out.println("Cleaned up discussionCommentRepository");

        this.discussionTopicRepository.deleteAll();
        System.out.println("Cleaned up discussionTopicRepository");
    }

    public ObjectNode getDefaultPatternContent() {
        ObjectNode content = this.objectMapper.createObjectNode();
        content.put("Field1", "FieldValue1");
        content.put("Field2", 123);
        return content;
    }

    public Pattern getDefaultPattern() {
        Pattern pattern = new Pattern();
        UUID uuid = UUID.randomUUID();
        pattern.setUri("http://patternpedia.org/testPatterns/TestPattern" + uuid);
        pattern.setName("TestPattern" + uuid);

        ObjectNode content = this.getDefaultPatternContent();
        pattern.setContent(content);
        return pattern;
    }

    public PatternLanguage getDefaultPatternLanguageWithSchema() throws MalformedURLException {
        PatternLanguage patternLanguage = this.getDefaultPatternLanguageWithoutSchema();
        patternLanguage.setPatternSchema(this.getDefaultPatternSchema());

        return patternLanguage;
    }

    public PatternLanguage getDefaultPatternLanguageWithoutSchema() throws MalformedURLException {
        PatternLanguage patternLanguage = new PatternLanguage();
        patternLanguage.setName("DefaultTestPatternLanguage");
        patternLanguage.setUri("http://patternpedia.org/patternlanguages/DefaultTestPatternLanguage");
        patternLanguage.setLogo(new URL("http://patternpedia.org/someLogo.png"));

        return patternLanguage;
    }

    public PatternSchema getDefaultPatternSchema() {
        PatternSchema patternSchema = new PatternSchema();

        PatternSectionSchema patternSectionSchema1 = new PatternSectionSchema();
        patternSectionSchema1.setName("Field1");
        patternSectionSchema1.setLabel("Field1");
        patternSectionSchema1.setPosition(0);
        patternSectionSchema1.setType("string");
        patternSectionSchema1.setPatternSchema(patternSchema);

        PatternSectionSchema patternSectionSchema2 = new PatternSectionSchema();
        patternSectionSchema2.setName("Field2");
        patternSectionSchema2.setLabel("Field2");
        patternSectionSchema2.setPosition(0);
        patternSectionSchema2.setType("number");
        patternSectionSchema2.setPatternSchema(patternSchema);

        List<PatternSectionSchema> patternSectionSchemas = new ArrayList<>();
        patternSectionSchemas.add(patternSectionSchema1);
        patternSectionSchemas.add(patternSectionSchema2);
        patternSchema.setPatternSectionSchemas(patternSectionSchemas);
        return patternSchema;
    }

    public PatternLanguage setUpPatternLanguage(int numberOfPatterns) throws Exception {
        PatternLanguage patternLanguage = this.getDefaultPatternLanguageWithSchema();

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

        patternLanguage = this.objectMapper.readValue(result1.getResponse().getContentAsString(), PatternLanguage.class);

        for (int i = 0; i < numberOfPatterns; i++) {
            Pattern pattern = this.addPatternToLanguage(patternLanguage);
            if (null == patternLanguage.getPatterns()) {
                patternLanguage.setPatterns(new ArrayList<>(Collections.singletonList(pattern)));
            } else {
                patternLanguage.getPatterns().add(pattern);
            }
        }
        return patternLanguage;
    }

    public PatternView getDefaultPatternView() {
        PatternView patternView = new PatternView();
        patternView.setName("Test Pattern View");
        patternView.setUri("https://patternpedia.org/patternViews/testPatternView");
        return patternView;
    }

    public PatternView setUpPatternView() throws Exception {
        PatternView patternView = this.getDefaultPatternView();

        MvcResult postResult = this.mockMvc.perform(
                post("/patternViews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(patternView))
        ).andExpect(status().isCreated()).andReturn();

        String location = postResult.getResponse().getHeader("Location");

        MvcResult getResult = this.mockMvc.perform(
                get(location)
        ).andExpect(status().isOk()).andReturn();

        return this.objectMapper.readValue(getResult.getResponse().getContentAsByteArray(), PatternView.class);
    }

    public Pattern addPatternToLanguage(PatternLanguage patternLanguage) throws Exception {
        Pattern pattern = this.getDefaultPattern();

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

        return this.objectMapper.readValue(getPatternResponse.getResponse().getContentAsByteArray(), Pattern.class);
    }

    public Image getDefaultImage() {
        Image image = new Image();
        image.setId(UUID.randomUUID());
        image.setFileName("testImage");
        image.setFileType("testType");
        image.setData("testFile".getBytes());
        return image;
    }

    public DiscussionTopic getDefaultDiscussionTopic() {
        DiscussionTopic discussionTopic = new DiscussionTopic();
        discussionTopic.setId(UUID.randomUUID());
        discussionTopic.setTitle("TestTopic");
        discussionTopic.setDescription("Testdescription");
        discussionTopic.setImageId(this.getDefaultImage().getId());
        return discussionTopic;
    }

    public DiscussionComment getDefaultDiscussionComment() {
        DiscussionComment discussionComment = new DiscussionComment();
        discussionComment.setId(UUID.randomUUID());
        discussionComment.setText("TestText");
        discussionComment.setDiscussionTopic(this.getDefaultDiscussionTopic());
        return discussionComment;
    }
}
