package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.Pattern;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RepositoryRestResource
public interface PatternRepository extends CrudRepository<Pattern, UUID> {

    public Optional<Pattern> findByUri(String uri);

    public boolean existsByUri(String uri);

}
