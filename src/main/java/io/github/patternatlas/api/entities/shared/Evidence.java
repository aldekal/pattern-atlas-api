package io.github.patternatlas.api.entities.shared;

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
public abstract class Evidence {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String title;
    private String context;
    private String type;
    private Boolean supporting;
    private String source;

    public Evidence(String title, String context, String type, Boolean supporting, String source) {
        this.title = title;
        this.context = context;
        this.type = type;
        this.supporting = supporting;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidence)) return false;
        Evidence that = (Evidence) o;
        return id.equals(that.id) &&
                title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Evidence: " + this.title + this.id.toString();
    }
}
