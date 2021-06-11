package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.candidate.Candidate;
import com.patternatlas.api.entities.candidate.rating.CandidateRating;
import com.patternatlas.api.entities.candidate.rating.CandidateRatingKey;
import com.patternatlas.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CandidateRatingRepository extends JpaRepository<CandidateRating, CandidateRatingKey> {

    List<CandidateRating> findAllByCandidate(Candidate candidate);

    List<UserEntity> findAllByUser(UserEntity user);

    CandidateRating findByCandidateAndUser(Candidate candidate, UserEntity user);
}
