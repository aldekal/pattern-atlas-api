package io.github.patternatlas.api.entities.issue.author;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.shared.CompositeKey;
import io.github.patternatlas.api.entities.user.UserEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Data
@NoArgsConstructor
public class IssueAuthor {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
    @EqualsAndHashCode.Include
    private Issue issue;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private String role;

    public IssueAuthor(Issue issue, UserEntity user) {
        this.issue = issue;
        this.user = user;
        this.id = new CompositeKey(issue.getId(), user.getId());
    }

    public IssueAuthor(Issue issue, UserEntity user, String role) {
        this(issue, user);
        this.role = role;
    }
}
