package com.patternpedia.api.rest.model.candidate;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.CommentModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CandidateModel {

    private UUID id;
    private String uri;
    private String name;
    private String iconUrl;
    private UUID patternLanguageId;
    private String patternLanguageName;
    private Object content;
    private String version;
    // RESPONSE
    // RESPONSE
    private Collection<UUID> upVotes = new ArrayList<>();
    private Collection<UUID> downVotes = new ArrayList<>();
    private List<AuthorModel> authors = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();

    public CandidateModel(Candidate candidate) {
        PatternLanguage patternLanguage = candidate.getPatternLanguage();
        this.id = candidate.getId();
        this.uri = candidate.getUri();
        this.name = candidate.getName();
        this.iconUrl = candidate.getIconUrl();
        this.content = candidate.getContent();
        this.version = candidate.getVersion();
        // RESPONSE
        for (CandidateRating candidateRating: candidate.getUserRating()) {
            if (candidateRating.getRating() == 1)
                this.upVotes.add(candidateRating.getUser().getId());
            if (candidateRating.getRating() == -1)
                this.downVotes.add(candidateRating.getUser().getId());
        }
        this.authors = candidate.getAuthors().stream().map(issueAuthor -> new AuthorModel(issueAuthor.getUser(), issueAuthor.getRole())).collect(Collectors.toList());
        this.comments = candidate.getComments().stream().map(issueComment -> CommentModel.from(issueComment)).collect(Collectors.toList());

        if (patternLanguage != null) {
            this.patternLanguageId = patternLanguage.getId();
            this.patternLanguageName = patternLanguage.getName();
        } else {
            this.patternLanguageId = null;
            this.patternLanguageName = "NONE";
        }
    }
}
