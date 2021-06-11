package com.patternatlas.api.rest.model;

import com.patternatlas.api.entities.DiscussionComment;
import com.patternatlas.api.entities.DiscussionTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiscussionTopicModel {
    private DiscussionTopic discussionTopic;
    private List<DiscussionComment> discussionComments;

    public DiscussionTopicModel (DiscussionTopic discussionTopic, List<DiscussionComment> discussionComments){
        this.discussionTopic = discussionTopic;
        this.discussionComments = discussionComments;
    }
}
