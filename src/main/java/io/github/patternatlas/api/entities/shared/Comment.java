package io.github.patternatlas.api.entities.shared;

import java.util.Objects;
import java.util.UUID;

import io.github.patternatlas.api.entities.user.UserEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Comment {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    public Comment(String text, UserEntity user) {
        this.text = text;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment that = (Comment) o;
        return id.equals(that.id) &&
                text.equals(that.text) &&
                user.equals(that.user);
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
