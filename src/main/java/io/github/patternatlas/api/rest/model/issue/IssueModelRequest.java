package io.github.patternatlas.api.rest.model.issue;

import io.github.patternatlas.api.rest.model.shared.AuthorModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class IssueModelRequest extends IssueModel {

    private Integer updateRating;
    private List<AuthorModel> updateAuthors;
}
