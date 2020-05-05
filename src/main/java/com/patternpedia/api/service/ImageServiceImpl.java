package com.patternpedia.api.service;

import com.patternpedia.api.entities.Image;
import com.patternpedia.api.repositories.ImageRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


import java.util.UUID;

@Service
@Validated
@Transactional
public class ImageServiceImpl implements ImageService{

    private ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Override
    public Image createImage(Image image) {
        if (null == image) {
            throw new NullPointerException();
        }

        return this.imageRepository.save(image);
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImageById(UUID imageId) {
        return this.imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @Override
    @Transactional
    public Image updateImage(Image image) {
        if (null == image) {
            throw new NullPointerException();
        }

        return this.imageRepository.save(image);
    }

    @Override
    @Transactional
    public void deleteImage(Image image) {
        if (null == image) {
            throw new NullPointerException();
        }
        this.imageRepository.save(image);
        this.imageRepository.deleteById(image.getId());
    }
}
