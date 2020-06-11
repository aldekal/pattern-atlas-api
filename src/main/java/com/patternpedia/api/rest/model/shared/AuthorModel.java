package com.patternpedia.api.rest.model.shared;

import com.patternpedia.api.entities.candidate.author.CandidateAuthor;
import com.patternpedia.api.entities.issue.author.IssueAuthor;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AuthorModel {

    private UUID userId;
    private String name;
    private String authorRole;

    public AuthorModel(IssueAuthor issueAuthor) {
        this.userId = issueAuthor.getUser().getId();
        this.name = issueAuthor.getUser().getName();
        this.authorRole = issueAuthor.getRole();
    }

    public AuthorModel(CandidateAuthor candidateAuthor) {
        this.userId = candidateAuthor.getUser().getId();
        this.name = candidateAuthor.getUser().getName();
        this.authorRole = candidateAuthor.getRole();
    }

    public AuthorModel(UserEntity user) {
        this.userId = user.getId();
        this.name = user.getName();
    }

    public AuthorModel(UserEntity user, String role) {
        this(user);
        this.authorRole = role;
    }
}
