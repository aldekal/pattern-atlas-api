package io.github.patternatlas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.candidate.CandidateComment;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.candidate.rating.CandidateCommentRating;
import io.github.patternatlas.api.entities.candidate.rating.CandidateCommentRatingKey;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRatingRepository extends JpaRepository<CandidateCommentRating, CandidateCommentRatingKey> {

    CandidateCommentRating findByCandidateCommentAndUser(CandidateComment candidateComment, UserEntity user);
}
