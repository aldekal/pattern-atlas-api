package com.patternpedia.api.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

//    @JsonIgnore
//    private Pattern pattern;

    private CandidateModel(Candidate candidate) {
        PatternLanguage patternLanguage = candidate.getPatternLanguage();
//        this.pattern = pattern;
        this.id = candidate.getId();
        this.uri = candidate.getUri();
        this.name = candidate.getName();
        this.iconUrl = candidate.getIconUrl();
        this.content = candidate.getContent();

        this.patternLanguageId = patternLanguage.getId();
        this.patternLanguageName = patternLanguage.getName();
    }

    public static CandidateModel from(Candidate candidate) {
        return new CandidateModel(candidate);
    }
}
