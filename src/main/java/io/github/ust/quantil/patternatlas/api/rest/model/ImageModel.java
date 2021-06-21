package io.github.ust.quantil.patternatlas.api.rest.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageModel {
    private byte[] image;
    private List<DiscussionTopicModel> topicModels;

    public ImageModel(byte[] image, List<DiscussionTopicModel> topicModels) {
        this.image = image;
        this.topicModels = topicModels;
    }
}
