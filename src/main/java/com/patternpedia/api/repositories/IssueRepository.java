package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

    public Optional<Issue> findByUri(String uri);
    public boolean existsByUri(String uri);
    public boolean existsByName(String name);
}
