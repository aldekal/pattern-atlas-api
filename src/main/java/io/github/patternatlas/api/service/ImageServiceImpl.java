package io.github.patternatlas.api.service;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import io.github.patternatlas.api.repositories.ImageRepository;
import io.github.patternatlas.api.entities.Image;

@Service
@Validated
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
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
