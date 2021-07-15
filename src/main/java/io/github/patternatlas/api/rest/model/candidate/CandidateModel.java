package io.github.patternatlas.api.rest.model.candidate;

import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.rest.model.shared.AuthorModel;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModel;

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
    private double ratingReadability = 0;
    private double ratingUnderstandability = 0;
    private double ratingAppropriateness = 0;
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
        this.readability = candidate.getUserRating().stream().map(candidateRating -> {
            this.ratingReadability += candidateRating.getReadability();
            return new RatingModel(candidateRating, candidateRating.getReadability());
        }).collect(Collectors.toList());
        this.understandability = candidate.getUserRating().stream().map(candidateRating -> {
            this.ratingUnderstandability += candidateRating.getUnderstandability();
            return new RatingModel(candidateRating, candidateRating.getUnderstandability());
        }).collect(Collectors.toList());
        this.appropriateness = candidate.getUserRating().stream().map(candidateRating -> {
            this.ratingAppropriateness += candidateRating.getAppropriateness();
            return new RatingModel(candidateRating, candidateRating.getAppropriateness());
        }).collect(Collectors.toList());

        this.ratingReadability = Math.round((this.ratingReadability / this.readability.size()) * 100.0) / 100.0;
        this.ratingUnderstandability = Math.round((this.ratingUnderstandability / this.understandability.size()) * 100.0) / 100.0;
        this.ratingAppropriateness = Math.round((this.ratingAppropriateness / this.appropriateness.size()) * 100.0) / 100.0;

        this.authors = candidate.getAuthors().stream().map(author -> new AuthorModel(author.getUser(), author.getRole())).collect(Collectors.toList());
        this.comments = candidate.getComments().stream().map(issueComment -> CommentModel.from(issueComment))
                .sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
                .collect(Collectors.toList());
        this.evidences = candidate.getEvidences().stream().map(candidateEvidence -> EvidenceModel.from(candidateEvidence))
                .sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
                .collect(Collectors.toList());

        if (patternLanguage != null) {
            this.patternLanguageId = patternLanguage.getId();
            this.patternLanguageName = patternLanguage.getName();
        } else {
            this.patternLanguageId = null;
            this.patternLanguageName = "NONE";
        }
    }
}
