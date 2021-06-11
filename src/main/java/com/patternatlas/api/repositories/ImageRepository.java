package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface ImageRepository extends CrudRepository<Image, UUID> {
}
