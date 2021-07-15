package io.github.patternatlas.api.rest.model.issue;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.rest.model.shared.AuthorModel;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IssueModel {

    private UUID id;
    private String uri;
    private String name;
    private String description;
    private String version;
    // RESPONSE
    private int rating;
    private Collection<UUID> upVotes = new ArrayList<>();
    private Collection<UUID> downVotes = new ArrayList<>();
    private List<AuthorModel> authors = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();
    private List<EvidenceModel> evidences = new ArrayList<>();

    public IssueModel(Issue issue) {
        this.id = issue.getId();
        this.uri = issue.getUri();
        this.name = issue.getName();
        this.description = issue.getDescription();
        this.version = issue.getVersion();
        //Response
        this.rating = 0;
        for (IssueRating issueRating: issue.getUserRating()) {
            if (issueRating.getRating() == 1) {
                this.upVotes.add(issueRating.getUser().getId());
                this.rating = this.rating + 1;
            }
            if (issueRating.getRating() == -1) {
                this.downVotes.add(issueRating.getUser().getId());
                this.rating = this.rating - 1;
            }
        }
        this.authors = issue.getAuthors().stream().map(issueAuthor -> new AuthorModel(issueAuthor.getUser(), issueAuthor.getRole())).collect(Collectors.toList());
        this.comments = issue.getComments().stream().map(issueComment -> CommentModel.from(issueComment))
                .sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
                .collect(Collectors.toList());
        this.evidences = issue.getEvidences().stream().map(issueEvidence -> EvidenceModel.from(issueEvidence))
                .sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
                .collect(Collectors.toList());
    }
}
