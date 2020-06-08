package com.patternpedia.api.service;

import com.patternpedia.api.repositories.DiscussionCommentRepository;
import com.patternpedia.api.repositories.DiscussionTopicRepository;
import com.patternpedia.api.entities.DiscussionComment;
import com.patternpedia.api.entities.DiscussionTopic;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;


@Service
@Validated
@Transactional
public class DiscussionServiceImpl implements  DiscussionService{

    private DiscussionTopicRepository discussionTopicRepository;
    private DiscussionCommentRepository discussionCommentRepository;

    public DiscussionServiceImpl(DiscussionTopicRepository discussionTopicRepository, DiscussionCommentRepository discussionCommentRepository){
        this.discussionCommentRepository =  discussionCommentRepository;
        this.discussionTopicRepository = discussionTopicRepository;
    }

    @Override
    public DiscussionTopic createTopic(DiscussionTopic topic) {
        return this.discussionTopicRepository.save(topic);
    }

    @Override
    public void deleteTopicById(UUID id) {
        for (DiscussionComment discussionComment: this.getTopicById(id).getDiscussionComments()){
            this.discussionCommentRepository.deleteById(discussionComment.getId());
        }
        this.discussionTopicRepository.deleteById(id);
    }

    @Override
    public DiscussionTopic getTopicById(UUID topicId) {
        return this.discussionTopicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @Override
    public DiscussionComment createComment(DiscussionComment topic) {
        return this.discussionCommentRepository.save(topic);
    }

    @Override
    public DiscussionComment getCommentById(UUID commentId) {
        return this.discussionCommentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @Override
    public void deleteCommentById(UUID id) {
        this.discussionCommentRepository.deleteById(id);
    }


}
