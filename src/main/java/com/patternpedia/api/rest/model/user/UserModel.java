package com.patternpedia.api.rest.model.user;

import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.rest.model.issue.IssueModel;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.shared.RatingModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserModel {

    private UUID id;
    private String role;
    private String email;
    private String name;
    private List<IssueModel> issues;
    private List<CommentModel> issueComments;
    private List<IssueModel> issueRatings;

    public UserModel(UserEntity user) {
        this.id = user.getId();
        this.role = user.getRole().getName();
        this.email = user.getEmail();
        this.name = user.getName();
        this.issues = user.getIssues().stream().map(issueAuthor -> new IssueModel(issueAuthor.getIssue())).collect(Collectors.toList());
        this.issueComments = user.getIssueComments().stream().map(issueComment -> new CommentModel(issueComment)).collect(Collectors.toList());
        this.issueRatings = user.getIssueRatings().stream().map((issueRating -> new IssueModel(issueRating.getIssue()))).collect(Collectors.toList());

    }
}
