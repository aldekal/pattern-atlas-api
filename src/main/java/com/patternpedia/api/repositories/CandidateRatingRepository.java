package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.rating.CandidateRating;
import com.patternpedia.api.entities.candidate.rating.CandidateRatingKey;
import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRatingRepository extends JpaRepository<CandidateRating, CandidateRatingKey> {

    List<CandidateRating> findAllByCandidate(Candidate candidate);

    List<UserEntity> findAllByUser(UserEntity user);

    CandidateRating findByCandidateAndUser(Candidate candidate, UserEntity user);
}
