package com.patternpedia.api.service;

import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.rest.model.DiscussionTopicModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DiscussionService {

    @Transactional
    DiscussionTopic createTopic(DiscussionTopic topic);

    @Transactional(readOnly = true)
    DiscussionTopic getTopicById(UUID topicId);

    @Transactional
    void deleteTopicById(UUID id);

    @Transactional
    DiscussionComment createComment(DiscussionComment topic);

    @Transactional(readOnly = true)
    DiscussionComment getCommentById(UUID commentId);

    @Transactional
    void deleteCommentById(UUID id);

    @Transactional
    List<DiscussionComment> getCommentsByTopicId(UUID topicId);

    @Transactional
    List<DiscussionTopic> getTopicsByImageId(UUID imageId);

    @Transactional
    List<DiscussionTopicModel> getTopicsAndCommentsByImageId(UUID imageId);

    @Transactional
    List<DiscussionTopic> updateTopicsByImageId(UUID oldImageId, UUID newImageId);
}
