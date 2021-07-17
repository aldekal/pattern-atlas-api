package io.github.patternatlas.api.entities.candidate.author;

import io.github.patternatlas.api.entities.candidate.Candidate;
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
public class CandidateAuthor {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
    @EqualsAndHashCode.Include
    private Candidate candidate;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private String role;

    public CandidateAuthor(Candidate candidate, UserEntity user) {
        this.candidate = candidate;
        this.user = user;
        this.id = new CompositeKey(candidate.getId(), user.getId());
    }

    public CandidateAuthor(Candidate candidate, UserEntity user, String role) {
        this(candidate, user);
        this.role = role;

    }
}
