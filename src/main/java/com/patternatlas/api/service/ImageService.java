package com.patternatlas.api.service;
import com.patternatlas.api.entities.Image;
import org.springframework.transaction.annotation.Transactional;

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
