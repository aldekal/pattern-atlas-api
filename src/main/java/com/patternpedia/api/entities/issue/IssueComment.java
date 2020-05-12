package com.patternpedia.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.issue.rating.IssueCommentRating;
import com.patternpedia.api.entities.shared.Comment;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class IssueComment extends Comment implements Serializable {

//    @Id
//    @GeneratedValue(generator = "pg-uuid")
//    private UUID id;
//
//    private String text;
//
//    private int rating = 0;

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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof IssueComment)) return false;
//        IssueComment that = (IssueComment) o;
//        return id.equals(that.id) &&
//                text.equals(that.text);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, text);
//    }
//
//    @Override
//    public String toString() {
//        return "Comment: " + this.text + this.id.toString() + this.rating;
//    }
}
