package io.github.patternatlas.api.entities.user.role;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;
        Privilege that = (Privilege) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Privileges: " + this.name;
    }


}
