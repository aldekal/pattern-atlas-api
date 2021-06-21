package io.github.ust.quantil.patternatlas.api.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Pattern extends EntityWithURI {

    private String iconUrl;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @JsonIgnore
    @OneToMany(mappedBy = "pattern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewPattern> patternViews = new ArrayList<>();

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @NotNull
    private Object content;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object renderedContent;
}
