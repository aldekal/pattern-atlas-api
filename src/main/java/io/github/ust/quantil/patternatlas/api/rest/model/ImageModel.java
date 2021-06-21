package io.github.ust.quantil.patternatlas.api.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ImageModel {
    private byte[] image;
    private List<DiscussionTopicModel> topicModels;

    public ImageModel(byte[] image, List<DiscussionTopicModel> topicModels){
        this.image = image;
        this.topicModels = topicModels;
    }
}
