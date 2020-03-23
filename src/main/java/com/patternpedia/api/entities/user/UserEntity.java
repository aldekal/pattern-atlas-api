package com.patternpedia.api.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.pattern.evolution.CommentPatternEvolution;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class UserEntity implements Serializable{

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.MEMBER;

    private String mail;

    private String name;

//    @ColumnTransformer(read = "pgp_sym_decrypt(password, 'mySecretKey')", write = "pgp_sym_encrypt(?, 'mySecretKey')")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingPatternEvolution> ratingPatternEvolutions = new HashSet<RatingPatternEvolution>();

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentPatternEvolution> comments = new ArrayList<>();

    public UserEntity(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
                mail.equals(that.mail) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mail, name);
    }

    @Override
    public String toString() {
        return "User: " + this.id.toString();
    }
}
