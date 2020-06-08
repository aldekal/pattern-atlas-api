package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}