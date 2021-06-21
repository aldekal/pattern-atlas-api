package io.github.ust.quantil.patternatlas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.candidate.CandidateComment;
import io.github.ust.quantil.patternatlas.api.entities.candidate.rating.CandidateCommentRating;
import io.github.ust.quantil.patternatlas.api.entities.candidate.rating.CandidateCommentRatingKey;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRatingRepository extends JpaRepository<CandidateCommentRating, CandidateCommentRatingKey> {

    CandidateCommentRating findByCandidateCommentAndUser(CandidateComment candidateComment, UserEntity user);
}
