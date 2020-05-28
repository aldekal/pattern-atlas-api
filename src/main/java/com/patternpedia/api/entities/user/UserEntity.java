package com.patternpedia.api.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.candidate.CandidateComment;
import com.patternpedia.api.entities.candidate.rating.CandidateRating;
import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.issue.rating.IssueRating;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
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

    /** User fields*/
    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Type(type = "pgsql_enum")
    private List<UserRole> roles = new ArrayList<>(Arrays.asList(UserRole.MEMBER));

    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** Issue fields*/
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IssueRating> issueRatings = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueComment> issueComments = new ArrayList<>();

    /** Candidate fields*/
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateRating> candidateRatings = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateComment> candidateComments = new ArrayList<>();

    /** Pattern fields*/
//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<CandidateRating> candidateRatings = new HashSet<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CandidateComment> candidateComments = new ArrayList<>();

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserEntity(String name, String email, String password, List<UserRole> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
                email.equals(that.email) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name);
    }

    @Override
    public String toString() {
        return "User: " + this.name;
    }
}
