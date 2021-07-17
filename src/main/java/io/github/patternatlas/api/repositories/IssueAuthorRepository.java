package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.issue.author.IssueAuthor;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueAuthorRepository extends JpaRepository<IssueAuthor, CompositeKey> {

    boolean existsByIdAndRole(CompositeKey compositeKey, String role);

}
