package io.github.patternatlas.api.rest.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.patternatlas.api.entities.Image;
import io.github.patternatlas.api.rest.model.ImageModel;
import io.github.patternatlas.api.service.DiscussionService;
import io.github.patternatlas.api.service.ImageService;

@RestController
@ConditionalOnExpression(value = "false")  // TODO: check if the ImageController can be removed entirely
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ImageController {

    private final ImageService imageService;
    private final DiscussionService discussionService;

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
    byte[] getImageById(@PathVariable UUID imageId) {
        // deepcode ignore XSS: Returning by service created content via uuid
        return this.imageService.getImageById(imageId).getData();
    }

    @PostMapping(
            value = "/update-image/{imageId}",
            produces = "image/svg+xml"
    )
    public @ResponseBody
    byte[] updateImage(@PathVariable UUID imageId, @RequestBody byte[] data) {
        Image image = new Image();
        image.setId(imageId);
        image.setData(data);
        image.setFileName(imageId.toString());
        image.setFileType("image/svg+xml");
        return this.imageService.updateImage(image).getData();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/add-image",
            produces = "application/json"
    )
    public @ResponseBody
    Image addImage(@RequestBody Image image) {
        // deepcode ignore XSS: <please specify a reason of ignoring this>
        return this.imageService.createImage(image);
    }

    @GetMapping(
            value = "/get-image-and-comments-by-id/{imageId}"
    )
    public @ResponseBody
    ImageModel getImageAndCommentsById(@PathVariable UUID imageId) {
        // deepcode ignore XSS: Returning by service created content via uuid
        return new ImageModel(this.imageService.getImageById(imageId).getData(), this.discussionService.getTopicsAndCommentsByImageId(imageId));
    }
}
