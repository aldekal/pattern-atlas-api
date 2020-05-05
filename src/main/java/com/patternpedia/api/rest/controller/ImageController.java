package com.patternpedia.api.rest.controller;



import com.fasterxml.jackson.databind.ObjectMapper;

import com.patternpedia.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(
            value = "/getImageById/{imageId}",
            produces = "image/svg+xml"
    )
    public @ResponseBody
    byte[] renderLatexAsPng(@PathVariable String imageId) throws IOException {
        UUID uuid = UUID.fromString(imageId);
        return this.imageService.getImageById(uuid).getData();
    }


}