package io.github.patternatlas.api.entities.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.patternatlas.api.entities.EntityWithURI;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.candidate.author.CandidateAuthor;
import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidence;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.rest.model.candidate.CandidateModelRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Candidate extends EntityWithURI {

    private String iconUrl;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @NotNull
    private Object content;

    private String version = "0.1.0";

    @JsonIgnore
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateAuthor> authors = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateRating> userRating = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateComment> comments = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateEvidence> evidences = new ArrayList<>();

    public Candidate (CandidateModelRequest candidateModelRequest) {
//        this.setId(candidateModel.getId());
        this.setUri(candidateModelRequest.getUri());
        this.setName(candidateModelRequest.getName());
        this.setIconUrl(candidateModelRequest.getIconUrl());
        this.setContent(candidateModelRequest.getContent());
        this.setVersion(candidateModelRequest.getVersion());
    }

    public void updateCandidate(CandidateModelRequest candidateModelRequest) {
        this.setId(candidateModelRequest.getId());
        this.setUri(candidateModelRequest.getUri());
        this.setName(candidateModelRequest.getName());
        this.setIconUrl(candidateModelRequest.getIconUrl());
        this.setContent(candidateModelRequest.getContent());
        this.setVersion(candidateModelRequest.getVersion());
    }

    public String toString() {
        return this.getId().toString();
    }
}