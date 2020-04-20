package com.patternpedia.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class CommentIssue implements Serializable{

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String text;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Issue issue;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    public CommentIssue(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentIssue)) return false;
        CommentIssue that = (CommentIssue) o;
        return id.equals(that.id) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        return "Comment: " + this.text + this.id.toString();
    }
}
