package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.rest.model.DiscussionTopicModel;
import com.patternpedia.api.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class DiscussionController {

    private DiscussionService discussionService;

    @Autowired
    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/add-topic"
    )
    public @ResponseBody
    DiscussionTopic addDiscussionTopic(@RequestBody DiscussionTopic topic){
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.createTopic(topic);
    }

    @DeleteMapping(
            value = "/delete-topic/{id}"
    )
    public @ResponseBody
    ResponseEntity<?> deleteDiscussionTopic(@PathVariable UUID topicId){
        this.discussionService.deleteTopicById(topicId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/add-comment/{topicId}"
    )
    public @ResponseBody
    DiscussionComment addDiscussionComment(@PathVariable UUID topicId, @RequestBody DiscussionComment comment){
        comment.setDiscussionTopic(this.discussionService.getTopicById(topicId));
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.createComment(comment);
    }

    @GetMapping(
            value = "/get-comments-by-topic/{topicId}"
    )
    public @ResponseBody
    List<DiscussionComment> getCommentsByTopic(@PathVariable UUID topicId){
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getCommentsByTopicId(topicId);
    }

    @GetMapping(
            value = "/get-topic-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopic> getTopicsByImageId(@PathVariable UUID imageId){
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getTopicsByImageId(imageId);
    }

    @GetMapping(
            value = "/get-topics-and-comments-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopicModel> getTopicsAndCommentsByImageId(@PathVariable UUID imageId){
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.discussionService.getTopicsAndCommentsByImageId(imageId);
    }
}