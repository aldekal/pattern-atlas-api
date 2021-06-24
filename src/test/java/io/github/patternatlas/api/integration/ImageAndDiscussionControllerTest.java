package io.github.patternatlas.api.integration;

import java.net.URI;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.patternatlas.api.util.IntegrationTestHelper;
import io.github.patternatlas.api.entities.DiscussionComment;
import io.github.patternatlas.api.entities.DiscussionTopic;
import io.github.patternatlas.api.entities.Image;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImageAndDiscussionControllerTest extends IntegrationTest {

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Before
    public void cleanUpReposBefore() {
        this.integrationTestHelper.cleanUpRepos();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testImagesAndDiscussions() throws Exception {
        //Test add-image
        Image image = this.integrationTestHelper.getDefaultImage();
        final String baseUrlAddImage = "http://localhost:" + port + "/add-image/";
        URI uriAddImage = new URI(baseUrlAddImage);
        HttpEntity<Image> requestAddImage = new HttpEntity<>(image);
        ResponseEntity<String> resultAddImage = this.restTemplate.postForEntity(uriAddImage, requestAddImage, String.class);
        Assert.assertEquals(201, resultAddImage.getStatusCodeValue());
        JSONObject jsonObjectAddImage = new JSONObject(resultAddImage.getBody());
        String imageId = jsonObjectAddImage.get("id").toString();

        //Test get-image-by-id
        final String baseUrlGetImage = "http://localhost:" + port + "/get-image-by-id/" + imageId;
        URI uriGetImage = new URI(baseUrlGetImage);
        ResponseEntity<String> resultGetImage = this.restTemplate.getForEntity(uriGetImage, String.class);
        Assert.assertEquals(200, resultGetImage.getStatusCodeValue());
        //deepcode ignore ReplaceBoxedConstructor~java.lang.String: String.valueOf and toString() doesnt work for byte[]
        Assert.assertEquals(resultGetImage.getBody(), new String(image.getData()));

        //Test update-image
        Image updatedImage = image;
        updatedImage.setId(UUID.fromString(imageId));
        updatedImage.setFileName("TestFilenameUpdate");
        final String baseUrlUpdateImage = "http://localhost:" + port + "/update-image/" + imageId;
        URI uriUpdateImage = new URI(baseUrlUpdateImage);
        HttpEntity<Image> requestUpdateImage = new HttpEntity<>(updatedImage);
        ResponseEntity<String> resultUpdateImage = this.restTemplate.postForEntity(uriUpdateImage, requestUpdateImage, String.class);
        Assert.assertEquals(200, resultUpdateImage.getStatusCodeValue());
        JSONObject jsonObjectUpdateImage = new JSONObject(resultUpdateImage.getBody());
        Assert.assertEquals(jsonObjectUpdateImage.get("fileName"), updatedImage.getFileName());

        //Test add-topic
        DiscussionTopic discussionTopic = this.integrationTestHelper.getDefaultDiscussionTopic();
        discussionTopic.setImageId(UUID.fromString(imageId));
        final String baseUrlAddTopic = "http://localhost:" + port + "/add-topic/";
        URI uriAddTopic = new URI(baseUrlAddTopic);
        HttpEntity<DiscussionTopic> requestAddTopic = new HttpEntity<>(discussionTopic);
        ResponseEntity<String> resultAddTopic = this.restTemplate.postForEntity(uriAddTopic, requestAddTopic, String.class);
        Assert.assertEquals(201, resultAddTopic.getStatusCodeValue());
        JSONObject jsonObjectAddTopic = new JSONObject(resultAddTopic.getBody());
        String topicId = jsonObjectAddTopic.get("id").toString();

        //Test add-comment
        DiscussionComment discussionComment = this.integrationTestHelper.getDefaultDiscussionComment();
        discussionComment.setDiscussionTopic(discussionTopic);
        final String baseUrlAddComment = "http://localhost:" + port + "/add-comment/" + topicId;
        URI uriAddComment = new URI(baseUrlAddComment);
        HttpEntity<DiscussionComment> requestAddComment = new HttpEntity<>(discussionComment);
        ResponseEntity<String> resultAddComment = this.restTemplate.postForEntity(uriAddComment, requestAddComment, String.class);
        Assert.assertEquals(201, resultAddComment.getStatusCodeValue());
        JSONObject jsonObjectAddComment = new JSONObject(resultAddComment.getBody());
        String commentId = jsonObjectAddComment.get("id").toString();

        //Test get-comments-by-topic
        final String baseUrlGetCommentsByTopic = "http://localhost:" + port + "/get-comments-by-topic/" + topicId;
        URI uriGetCommentsByTopic = new URI(baseUrlGetCommentsByTopic);
        ResponseEntity<String> resultGetCommentsByTopic = this.restTemplate.getForEntity(uriGetCommentsByTopic, String.class);
        Assert.assertEquals(200, resultGetCommentsByTopic.getStatusCodeValue());
        JSONArray jsonObjectGetCommentByTopic = new JSONArray(resultGetCommentsByTopic.getBody());
        String getCommentText = jsonObjectGetCommentByTopic.getJSONObject(0).get("text").toString();
        Assert.assertEquals(getCommentText, discussionComment.getText());

        //Test get-image-and-comments-by-id
        final String baseUrlGetImageAndCommentsById = "http://localhost:" + port + "/get-image-and-comments-by-id/" + imageId;
        URI uriGetImageAndCommentsById = new URI(baseUrlGetImageAndCommentsById);
        ResponseEntity<String> resultGetImageAndCommentsById = this.restTemplate.getForEntity(uriGetImageAndCommentsById, String.class);
        Assert.assertEquals(200, resultGetImageAndCommentsById.getStatusCodeValue());
        JSONObject jsonObjectGetImageAndCommentsById = new JSONObject(resultGetImageAndCommentsById.getBody());
        // deepcode ignore ReplaceBoxedConstructor~java.lang.String: String.valueOf and toString() doesnt work for byte[]
        String getImage = new String(Base64.getDecoder().decode(jsonObjectGetImageAndCommentsById.get("image").toString()));
        Assert.assertTrue(getImage.contains(image.getFileType()));
        String topicDescription = jsonObjectGetImageAndCommentsById.getJSONArray("topicModels").getJSONObject(0).getJSONObject("discussionTopic").get("description").toString();
        Assert.assertEquals(topicDescription, discussionTopic.getDescription());
        String commentText = jsonObjectGetImageAndCommentsById.getJSONArray("topicModels").getJSONObject(0).getJSONArray("discussionComments").getJSONObject(0).get("text").toString();
        Assert.assertEquals(commentText, discussionComment.getText());

        //Test get-topic-by-image
        final String baseUrlGetTopicByImage = "http://localhost:" + port + "/get-topic-by-image/" + imageId;
        URI uriGetTopicByImage = new URI(baseUrlGetTopicByImage);
        ResponseEntity<String> resultGetTopicByImage = this.restTemplate.getForEntity(uriGetTopicByImage, String.class);
        Assert.assertEquals(200, resultGetTopicByImage.getStatusCodeValue());
        JSONArray jsonObjectGetTopicByImage = new JSONArray(resultGetTopicByImage.getBody());
        String getDescription = jsonObjectGetTopicByImage.getJSONObject(0).get("description").toString();
        Assert.assertEquals(getDescription, discussionTopic.getDescription());

        //Test get-topic-and-comments-by-image
        final String baseUrlGetTopicsAndCommentsByImage = "http://localhost:" + port + "/get-topics-and-comments-by-image/" + imageId;
        URI uriGetTopicsAndCommentsByImage = new URI(baseUrlGetTopicsAndCommentsByImage);
        ResponseEntity<String> resultGetTopicsAndCommentsByImage = this.restTemplate.getForEntity(uriGetTopicsAndCommentsByImage, String.class);
        Assert.assertEquals(200, resultGetTopicsAndCommentsByImage.getStatusCodeValue());
        JSONArray jsonObjectGetTopicsAndCommentsByImage = new JSONArray(resultGetTopicsAndCommentsByImage.getBody());
        String description = jsonObjectGetTopicsAndCommentsByImage.getJSONObject(0).getJSONObject("discussionTopic").get("description").toString();
        Assert.assertEquals(description, discussionTopic.getDescription());
        String commentText1 = jsonObjectGetTopicsAndCommentsByImage.getJSONObject(0).getJSONArray("discussionComments").getJSONObject(0).get("text").toString();
        Assert.assertEquals(commentText1, discussionComment.getText());

        //Test delete-topic
        final String baseUrlDeleteTopic = "http://localhost:" + port + "/delete-topic/" + topicId;
        URI uriDeleteTopic = new URI(baseUrlDeleteTopic);
        this.restTemplate.delete(uriDeleteTopic);
        ResponseEntity<String> resultGetCommentsByTopicAfterDelete = this.restTemplate.getForEntity(uriGetCommentsByTopic, String.class);
        Assert.assertEquals(404, resultGetCommentsByTopicAfterDelete.getStatusCodeValue());
    }
}
