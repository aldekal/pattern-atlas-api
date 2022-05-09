package io.github.patternatlas.api.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.patternatlas.api.rest.model.DiscussionTopicModel;
import io.github.patternatlas.api.entities.DiscussionComment;
import io.github.patternatlas.api.entities.DiscussionTopic;
import io.github.patternatlas.api.service.DiscussionService;

@RestController
@ConditionalOnExpression(value = "false")  // TODO: check if discussions can be removed completely
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class DiscussionController {

    private final DiscussionService discussionService;

    @Autowired
    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/add-topic"
    )
    public @ResponseBody
    DiscussionTopic addDiscussionTopic(@RequestBody DiscussionTopic topic) {
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.createTopic(topic);
    }

    @DeleteMapping(
            value = "/delete-topic/{topicId}"
    )
    public @ResponseBody
    ResponseEntity<?> deleteDiscussionTopic(@PathVariable UUID topicId) {
        this.discussionService.deleteTopicById(topicId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/add-comment/{topicId}"
    )
    public @ResponseBody
    DiscussionComment addDiscussionComment(@PathVariable UUID topicId, @RequestBody DiscussionComment comment) {
        comment.setDiscussionTopic(this.discussionService.getTopicById(topicId));
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.createComment(comment);
    }

    @GetMapping(
            value = "/get-comments-by-topic/{topicId}"
    )
    public @ResponseBody
    List<DiscussionComment> getCommentsByTopic(@PathVariable UUID topicId) {
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getCommentsByTopicId(topicId);
    }

    @GetMapping(
            value = "/get-topic-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopic> getTopicsByImageId(@PathVariable UUID imageId) {
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getTopicsByImageId(imageId);
    }

    @GetMapping(
            value = "/get-topics-and-comments-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopicModel> getTopicsAndCommentsByImageId(@PathVariable UUID imageId) {
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getTopicsAndCommentsByImageId(imageId);
    }
}
