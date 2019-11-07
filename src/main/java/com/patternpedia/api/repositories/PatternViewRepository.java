package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

public interface PatternViewRepository extends CrudRepository<PatternView, UUID> {

    public Optional<PatternView> findByUri(String uri);

    public boolean existsByUri(String uri);

}
