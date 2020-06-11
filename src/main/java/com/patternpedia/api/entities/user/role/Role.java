package com.patternpedia.api.entities.user.role;

import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<UserEntity> users;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Privilege> privileges;

    public Role(String name) {
        this.name = name;
    }


}
