package com.patternpedia.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.issue.author.IssueAuthor;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.rest.model.issue.IssueModelRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Issue extends EntityWithURI {

    private String description;

    private String version = "0.1.0";

    @JsonIgnore
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueAuthor> authors = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueRating> userRating = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueComment> comments = new ArrayList<>();

    public Issue (IssueModelRequest issueModelRequest) {
        this.setUri(issueModelRequest.getUri());
        this.setName(issueModelRequest.getName());
        this.setDescription(issueModelRequest.getDescription());
        this.setVersion(issueModelRequest.getVersion());
    }

    public void updateIssue(IssueModelRequest issueModelRequest) {
        this.setUri(issueModelRequest.getUri());
        this.setName(issueModelRequest.getName());
        this.setDescription(issueModelRequest.getDescription());
        this.setVersion(issueModelRequest.getVersion());
    }

    public String toString() {
        return this.getId().toString() + this.getDescription() + this.getComments().toString();
    }

}
