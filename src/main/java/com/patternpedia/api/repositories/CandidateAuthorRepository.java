package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.author.CandidateAuthor;
import com.patternpedia.api.entities.shared.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateAuthorRepository extends JpaRepository<CandidateAuthor, CompositeKey> {

    boolean existsByIdAndRole(CompositeKey compositeKey, String role);

}