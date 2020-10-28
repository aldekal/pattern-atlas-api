package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface ImageRepository extends CrudRepository<Image, UUID> {
}
