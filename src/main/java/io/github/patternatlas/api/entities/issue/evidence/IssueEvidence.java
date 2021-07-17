package io.github.patternatlas.api.entities.issue.evidence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.patternatlas.api.entities.issue.Issue;
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
public class IssueEvidence extends Evidence implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Issue issue;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "issueEvidence", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueEvidenceRating> userRating = new ArrayList<>();

    public IssueEvidence(String title, String context, String type, Boolean supporting, String source, Issue issue, UserEntity user) {
        super(title, context, type, supporting, source);
        this.issue = issue;
        this.user = user;
    }

    public void updateEvidence(String title, String context, String type, Boolean supporting, String source) {
        this.setTitle(title);
        this.setContext(context);
        this.setType(type);
        this.setSupporting(supporting);
        this.setSource(source);
    }
}
