package io.github.patternatlas.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import io.github.patternatlas.api.repositories.DiscussionCommentRepository;
import io.github.patternatlas.api.repositories.DiscussionTopicRepository;
import io.github.patternatlas.api.rest.model.DiscussionTopicModel;
import io.github.patternatlas.api.entities.DiscussionComment;
import io.github.patternatlas.api.entities.DiscussionTopic;

@Service
@Validated
@Transactional
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionTopicRepository discussionTopicRepository;
    private final DiscussionCommentRepository discussionCommentRepository;

    public DiscussionServiceImpl(DiscussionTopicRepository discussionTopicRepository, DiscussionCommentRepository discussionCommentRepository) {
        this.discussionCommentRepository = discussionCommentRepository;
        this.discussionTopicRepository = discussionTopicRepository;
    }

    @Override
    public DiscussionTopic createTopic(DiscussionTopic topic) {
        return this.discussionTopicRepository.save(topic);
    }

    @Override
    public void deleteTopicById(UUID id) {
        for (DiscussionComment discussionComment : this.getTopicById(id).getDiscussionComments()) {
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

    @Override
    public List<DiscussionComment> getCommentsByTopicId(UUID topicId) {
        return this.discussionCommentRepository.findDiscussionCommentByDiscussionTopic(this.getTopicById(topicId));
    }

    @Override
    public List<DiscussionTopic> getTopicsByImageId(UUID imageId) {
        return this.discussionTopicRepository.findDiscussionTopicsByImageId(imageId);
    }

    @Override
    public List<DiscussionTopicModel> getTopicsAndCommentsByImageId(UUID imageId) {
        List<DiscussionTopicModel> topicModelList = new ArrayList<>();
        this.discussionTopicRepository.findDiscussionTopicsByImageId(imageId).forEach(topic -> {
            DiscussionTopicModel topicModel = new DiscussionTopicModel(topic, this.discussionCommentRepository.findDiscussionCommentByDiscussionTopic(this.getTopicById(topic.getId())));
            topicModelList.add(topicModel);
        });
        return topicModelList;
    }

    @Override
    public List<DiscussionTopic> updateTopicsByImageId(UUID oldImageId, UUID newImageId) {
        this.discussionTopicRepository.findDiscussionTopicsByImageId(oldImageId).forEach(topic -> {
            topic.setImageId(newImageId);
            this.discussionTopicRepository.save(topic);
        });
        return this.discussionTopicRepository.findDiscussionTopicsByImageId(newImageId);
    }
}
