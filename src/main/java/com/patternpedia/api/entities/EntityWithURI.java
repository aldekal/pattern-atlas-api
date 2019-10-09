package com.patternpedia.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class EntityWithURI {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true)
    private String uri;

    @Column(nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityWithURI)) return false;
        EntityWithURI that = (EntityWithURI) o;
        return id.equals(that.id) &&
                uri.equals(that.uri) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri, name);
    }


}