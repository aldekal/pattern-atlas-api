package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.candidate.CandidateComment;
import com.patternatlas.api.entities.candidate.rating.CandidateCommentRating;
import com.patternatlas.api.entities.candidate.rating.CandidateCommentRatingKey;
import com.patternatlas.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRatingRepository extends JpaRepository<CandidateCommentRating, CandidateCommentRatingKey> {

    CandidateCommentRating findByCandidateCommentAndUser(CandidateComment candidateComment, UserEntity user);
}
