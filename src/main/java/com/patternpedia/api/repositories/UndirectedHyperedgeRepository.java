package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.UndirectedHyperedge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface UndirectedHyperedgeRepository extends CrudRepository<UndirectedHyperedge, Long> {
}
