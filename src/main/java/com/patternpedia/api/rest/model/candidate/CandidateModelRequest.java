package com.patternpedia.api.rest.model.candidate;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.user.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CandidateModelRequest extends CandidateModel {

    private Integer updateRating;
    private List<AuthorModel> updateAuthors;
}
