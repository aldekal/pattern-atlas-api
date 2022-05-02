package io.github.patternatlas.api.entities.user.role;

import io.github.patternatlas.api.entities.user.UserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    @ManyToMany(cascade = CascadeType.ALL)
    /*@JoinTable(
        name = "role_privileges",
        joinColumns = { @JoinColumn(name = "roles_id") },
        inverseJoinColumns = { @JoinColumn(name = "privileges_id") }
    )*/
    private Collection<Privilege> privileges;

    public Role(String name) {
        this.name = name;
    }

    public boolean checkPrivilege(String privilege) {
       for (Privilege p : this.privileges) {
           if (p.getName().equals(privilege)) {
               return true;
           }
       }
       return false;
    }

    public void removePrivilege(Privilege privilege) {
        this.privileges.remove(privilege);
        privilege.getRoles().remove(this);
    }

    public void addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
        privilege.getRoles().add(this);
    }

}
