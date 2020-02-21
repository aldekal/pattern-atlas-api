package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Entity
//@Data
////@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternEvolution extends EntityWithURI {

//    @Id
//    @GeneratedValue(generator = "pg-uuid")
//    private UUID id;
//
//    private String name;

    private int votes;

//    public PatternEvolution(String name) {
//        this.name = name;
//    }

//    @NaturalId(mutable = true)
//    @Column(nullable = false, unique = true)
//    private String uri;
//
//    PatternEvolution(String uri) {
//        this.uri = uri;
//    }

//    @Column(nullable = false)
//    private String name;

//    private String iconUrl;

//    @JsonIgnore
//    @ToString.Exclude
//    @ManyToOne
//    private PatternLanguage patternLanguage;

//    @JsonIgnore
//    @OneToMany(mappedBy = "pattern", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PatternViewPattern> patternViews = new ArrayList<>();

//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
//    @NotNull
//    private Object content;
}
