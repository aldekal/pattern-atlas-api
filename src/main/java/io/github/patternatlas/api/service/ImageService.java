package io.github.patternatlas.api.service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import io.github.patternatlas.api.entities.Image;

public interface ImageService {

    @Transactional
    Image createImage(Image image);

    @Transactional(readOnly = true)
    Image getImageById(UUID imageId);

    @Transactional
    Image updateImage(Image image);

    @Transactional
    void deleteImage(Image image);
}
