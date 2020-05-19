package com.patternpedia.api.entities.shared;

import com.patternpedia.api.entities.issue.IssueComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Comment {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String text;

    private int rating = 0;

    public Comment(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment that = (Comment) o;
        return id.equals(that.id) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        return "Comment: " + this.text + this.id.toString() + this.rating;
    }
}
