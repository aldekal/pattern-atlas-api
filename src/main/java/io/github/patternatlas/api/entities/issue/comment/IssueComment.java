package io.github.patternatlas.api.entities.issue.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.shared.Comment;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IssueComment extends Comment implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Issue issue;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "issueComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueCommentRating> userRating = new ArrayList<>();

    public IssueComment(String text, Issue issue, UserEntity user) {
        super(text);
        this.issue = issue;
        this.user = user;
    }

    public void updateComment(String text) {
        this.setText(text);
    }
}
