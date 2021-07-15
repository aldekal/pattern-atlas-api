package com.patternpedia.api.rest.model.candidate;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.user.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CandidateModelRequest extends CandidateModel {

    private UUID issueId;
    private Integer updateRating;
    private List<AuthorModel> updateAuthors;
}
