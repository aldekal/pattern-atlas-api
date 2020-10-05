package com.patternpedia.api.rest.model.candidate;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.shared.EvidenceModel;
import com.patternpedia.api.rest.model.shared.RatingModel;
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
    private Collection<RatingModel> readability = new ArrayList<>();
    private Collection<RatingModel> understandability = new ArrayList<>();
    private Collection<RatingModel> appropriateness = new ArrayList<>();
    private List<AuthorModel> authors = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();
    private List<EvidenceModel> evidences = new ArrayList<>();


    public CandidateModel(Candidate candidate) {
        PatternLanguage patternLanguage = candidate.getPatternLanguage();
        this.id = candidate.getId();
        this.uri = candidate.getUri();
        this.name = candidate.getName();
        this.iconUrl = candidate.getIconUrl();
        this.content = candidate.getContent();
        this.version = candidate.getVersion();
        // RESPONSE
        this.readability = candidate.getUserRating().stream().map(candidateRating -> new RatingModel(candidateRating, candidateRating.getReadability())).collect(Collectors.toList());
        this.understandability = candidate.getUserRating().stream().map(candidateRating -> new RatingModel(candidateRating, candidateRating.getUnderstandability())).collect(Collectors.toList());
        this.appropriateness = candidate.getUserRating().stream().map(candidateRating -> new RatingModel(candidateRating, candidateRating.getAppropriateness())).collect(Collectors.toList());
        this.authors = candidate.getAuthors().stream().map(issueAuthor -> new AuthorModel(issueAuthor.getUser(), issueAuthor.getRole())).collect(Collectors.toList());
        this.comments = candidate.getComments().stream().map(issueComment -> CommentModel.from(issueComment)).collect(Collectors.toList());
        this.evidences = candidate.getEvidences().stream().map(candidateEvidence -> EvidenceModel.from(candidateEvidence)).collect(Collectors.toList());

        if (patternLanguage != null) {
            this.patternLanguageId = patternLanguage.getId();
            this.patternLanguageName = patternLanguage.getName();
        } else {
            this.patternLanguageId = null;
            this.patternLanguageName = "NONE";
        }
    }
}
