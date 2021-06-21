package io.github.ust.quantil.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import io.github.ust.quantil.patternatlas.api.entities.DiscussionComment;
import io.github.ust.quantil.patternatlas.api.entities.DiscussionTopic;
import io.github.ust.quantil.patternatlas.api.rest.model.DiscussionTopicModel;

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
