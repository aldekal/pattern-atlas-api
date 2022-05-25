package io.github.patternatlas.api.entities.issue.comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.shared.Comment;
import io.github.patternatlas.api.entities.user.UserEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IssueComment extends Comment implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Issue issue;

    @JsonIgnore
    @OneToMany(mappedBy = "issueComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueCommentRating> userRating = new ArrayList<>();

    private int rating = 0;

    public IssueComment(String text, Issue issue, UserEntity user) {
        super(text, user);
        this.issue = issue;
    }

    public void updateComment(String text) {
        this.setText(text);
    }
}
