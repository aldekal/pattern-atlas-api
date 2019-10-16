package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSectionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface PatternSectionTypeRepository extends CrudRepository<PatternSectionType, Long> {
}
