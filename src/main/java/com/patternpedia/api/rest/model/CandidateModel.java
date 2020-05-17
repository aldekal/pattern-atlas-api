package com.patternpedia.api.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private String content;

    private String version;

    private int rating;

    private List<CandidateComment> comments;

    private CandidateModel(Candidate candidate) {
        PatternLanguage patternLanguage = candidate.getPatternLanguage();
        this.id = candidate.getId();
        this.uri = candidate.getUri();
        this.name = candidate.getName();
        this.iconUrl = candidate.getIconUrl();
        this.content = candidate.getContent();
        this.version = candidate.getVersion();
        this.rating = candidate.getRating();
        this.comments = candidate.getComments();

        if (patternLanguage != null) {
            this.patternLanguageId = patternLanguage.getId();
            this.patternLanguageName = patternLanguage.getName();
        } else {
            this.patternLanguageId = null;
            this.patternLanguageName = "NONE";
        }
    }

    public static CandidateModel from(Candidate candidate) {
        return new CandidateModel(candidate);
    }
}
