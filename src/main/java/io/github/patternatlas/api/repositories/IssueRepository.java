package io.github.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.issue.Issue;

@RepositoryRestResource(exported = false)
public interface IssueRepository extends JpaRepository<Issue, UUID> {

    public Optional<Issue> findByUri(String uri);
    public boolean existsByUri(String uri);
    public boolean existsByName(String name);
}
