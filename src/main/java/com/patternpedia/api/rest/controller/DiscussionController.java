package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.rest.model.DiscussionTopicModel;
import com.patternpedia.api.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping(
            value = "/add-topic"
    )
    public @ResponseBody
    DiscussionTopic addDiscussionTopic(@RequestBody DiscussionTopic topic){
        System.out.print(topic);
        return this.discussionService.createTopic(topic);
    }

    @DeleteMapping(
            value = "/delete-topic/{id}"
    )
    public @ResponseBody
    ResponseEntity<?> deleteDiscussionTopic(@PathVariable String id){
        UUID uuid = UUID.fromString(id);
        this.discussionService.deleteTopicById(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/add-comment/{topicId}"
    )
    public @ResponseBody
    DiscussionComment addDiscussionComment(@PathVariable String topicId, @RequestBody DiscussionComment comment){
        comment.setDiscussionTopic(this.discussionService.getTopicById(UUID.fromString(topicId)));
        return this.discussionService.createComment(comment);
    }

    @GetMapping(
            value = "/get-comments-by-topic/{topicId}"
    )
    public @ResponseBody
    List<DiscussionComment> getCommentsByTopic(@PathVariable String topicId){
        UUID uuid = UUID.fromString(topicId);
        return this.discussionService.getCommentsByTopicId(uuid);
    }


    @GetMapping(
            value = "/get-topic-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopic> getTopicsByImageId(@PathVariable String imageId){
        UUID uuid = UUID.fromString(imageId);
        return this.discussionService.getTopicsByImageId(uuid);
    }

    @GetMapping(
            value = "/get-topics-and-comments-by-image/{imageId}"
    )
    public @ResponseBody
    List<DiscussionTopicModel> getTopicsAndCommentsByImageId(@PathVariable String imageId){
        UUID uuid = UUID.fromString(imageId);
        return this.discussionService.getTopicsAndCommentsByImageId(uuid);
    }


}