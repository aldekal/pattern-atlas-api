package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.Image;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.rest.model.ImageModel;
import com.patternpedia.api.service.DiscussionService;
import com.patternpedia.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ImageController {

    private ImageService imageService;
    private DiscussionService discussionService;

    @Autowired
    public ImageController(ImageService imageService, DiscussionService discussionService) {
        this.imageService = imageService;
        this.discussionService = discussionService;
    }


    @GetMapping(
            value = "/get-image-by-id/{imageId}",
            produces = "image/svg+xml"
    )
    public @ResponseBody
    byte[] getImageById(@PathVariable String imageId){
        UUID uuid = UUID.fromString(imageId);
        return this.imageService.getImageById(uuid).getData();
    }

    @PostMapping(
            value = "/update-image/{imageId}",
            produces = "image/svg+xml"
    )
    public @ResponseBody
    byte[] updateImage(@PathVariable String imageId, @RequestBody byte[] data){
        UUID uuid = UUID.fromString(imageId);
        Image image = new Image();
        image.setId(uuid);
        image.setData(data);
        image.setFileName(imageId);
        image.setFileType("image/svg+xml");
        return this.imageService.updateImage(image).getData();
    }

    @GetMapping(
            value = "/get-image-and-comments-by-id/{imageId}"
    )
    public @ResponseBody
    ImageModel getImageAndCommentsById(@PathVariable String imageId){
        UUID uuid = UUID.fromString(imageId);
        return new ImageModel(this.imageService.getImageById(uuid).getData(), this.discussionService.getTopicsAndCommentsByImageId(uuid));
    }
}