package io.github.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.Image;

@RepositoryRestResource(exported = false)
public interface ImageRepository extends CrudRepository<Image, UUID> {
}
