package com.patternpedia.api.service;

import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import org.springframework.transaction.annotation.Transactional;

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

}
