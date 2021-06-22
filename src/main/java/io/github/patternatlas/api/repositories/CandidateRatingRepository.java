package io.github.patternatlas.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.candidate.rating.CandidateRating;
import io.github.patternatlas.api.entities.candidate.rating.CandidateRatingKey;

@RepositoryRestResource(exported = false)
public interface CandidateRatingRepository extends JpaRepository<CandidateRating, CandidateRatingKey> {

    List<CandidateRating> findAllByCandidate(Candidate candidate);

    List<UserEntity> findAllByUser(UserEntity user);

    CandidateRating findByCandidateAndUser(Candidate candidate, UserEntity user);
}
