package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface IssueRepository extends JpaRepository<Issue, UUID> {

    public Optional<Issue> findByUri(String uri);

    public boolean existsByUri(String uri);


}
