package com.patternpedia.api.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.issue.CommentIssue;
import com.patternpedia.api.entities.rating.RatingIssue;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class UserEntity implements Serializable{

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

//    @Enumerated( EnumArrayType.SQL_ARRAY_TYPE)
//    @OneToMany
//@Parameter(
////        name = EnumArrayType.SQL_ARRAY_TYPE,
////        value = "sensor_state"
////)
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Type(type = "pgsql_enum")
    private List<UserRole> role = new ArrayList<>(Arrays.asList(UserRole.MEMBER));

    private String mail;

    private String name;

//    @ColumnTransformer(read = "pgp_sym_decrypt(password, 'mySecretKey')", write = "pgp_sym_encrypt(?, 'mySecretKey')")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingIssue> ratingIssues = new HashSet<RatingIssue>();

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentIssue> comments = new ArrayList<>();

    public UserEntity(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    public UserEntity(String name, String mail, String password, List<UserRole> role) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.role = role;
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
