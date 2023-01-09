package io.github.patternatlas.api.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Pattern extends EntityWithURI {

    private String iconUrl;

    private String paperRef;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "pattern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewPattern> patternViews = new ArrayList<>();

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @NotNull
    private Object content;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object renderedContent;

    @Column(columnDefinition = "boolean default false")
    private Boolean deploymentModelingStructurePattern;

    @Column(columnDefinition = "boolean default false")
    private Boolean deploymentModelingBehaviorPattern;
}
