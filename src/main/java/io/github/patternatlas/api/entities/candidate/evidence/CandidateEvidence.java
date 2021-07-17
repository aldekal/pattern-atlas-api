package io.github.patternatlas.api.entities.candidate.evidence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.entities.shared.Evidence;
import io.github.patternatlas.api.entities.user.UserEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CandidateEvidence extends Evidence implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Candidate candidate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "candidateEvidence", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateEvidenceRating> userRating = new ArrayList<>();

    public CandidateEvidence(String title, String context, String type, Boolean supporting, String source, Candidate candidate, UserEntity user) {
        super(title, context, type, supporting, source);
        this.candidate = candidate;
        this.user = user;
    }

    public CandidateEvidence(IssueEvidence issueEvidence, Candidate candidate, UserEntity user) {
        this(issueEvidence.getTitle(), issueEvidence.getContext(), issueEvidence.getType(), issueEvidence.getSupporting(), issueEvidence.getSource(), candidate, user);
    }

    public void updateEvidence(String title, String context, String type, Boolean supporting, String source) {
        this.setTitle(title);
        this.setContext(context);
        this.setType(type);
        this.setSupporting(supporting);
        this.setSource(source);
    }
}