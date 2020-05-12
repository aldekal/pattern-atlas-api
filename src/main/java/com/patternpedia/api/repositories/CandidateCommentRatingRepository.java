package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.CandidateComment;
import com.patternpedia.api.entities.candidate.rating.CandidateCommentRating;
import com.patternpedia.api.entities.candidate.rating.CandidateCommentRatingKey;
import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.issue.rating.IssueCommentRating;
import com.patternpedia.api.entities.issue.rating.IssueCommentRatingKey;
import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRatingRepository extends JpaRepository<CandidateCommentRating, CandidateCommentRatingKey> {

    CandidateCommentRating findByCandidateCommentAndUser(CandidateComment candidateComment, UserEntity user);
}
