package com.patternpedia.api.service;
import com.patternpedia.api.entities.Image;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
