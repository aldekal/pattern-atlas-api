package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.candidate.author.CandidateAuthor;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateAuthorRepository extends JpaRepository<CandidateAuthor, CompositeKey> {

    boolean existsByIdAndRole(CompositeKey compositeKey, String role);

}