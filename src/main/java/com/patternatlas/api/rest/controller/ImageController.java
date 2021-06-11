package com.patternatlas.api.rest.controller;

import com.patternatlas.api.entities.Image;
import com.patternatlas.api.rest.model.ImageModel;
import com.patternatlas.api.service.DiscussionService;
import com.patternatlas.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    byte[] getImageById(@PathVariable UUID imageId){
        // deepcode ignore XSS: Returning by service created content via uuid
        return this.imageService.getImageById(imageId).getData();
    }

    @PostMapping(
            value = "/update-image/{imageId}",
            produces = "image/svg+xml"
    )
    public @ResponseBody
    byte[] updateImage(@PathVariable UUID imageId, @RequestBody byte[] data){
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
    Image addImage(@RequestBody Image image){
        // deepcode ignore XSS: <please specify a reason of ignoring this>
       return this.imageService.createImage(image);

    }

    @GetMapping(
            value = "/get-image-and-comments-by-id/{imageId}"
    )
    public @ResponseBody
    ImageModel getImageAndCommentsById(@PathVariable UUID imageId){
        // deepcode ignore XSS: Returning by service created content via uuid
        return new ImageModel(this.imageService.getImageById(imageId).getData(), this.discussionService.getTopicsAndCommentsByImageId(imageId));
    }
}
