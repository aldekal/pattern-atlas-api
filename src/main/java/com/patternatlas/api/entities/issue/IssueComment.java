package com.patternatlas.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternatlas.api.entities.issue.rating.IssueCommentRating;
import com.patternatlas.api.entities.shared.Comment;
import com.patternatlas.api.entities.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
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
    private Set<IssueCommentRating> userRating = new HashSet<>();

    public IssueComment(String text) {
        super(text);
    }
}
