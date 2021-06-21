package io.github.ust.quantil.patternatlas.api.entities.designmodel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DesignModelEdgeType {

    @Id
    private String name;

    private Boolean swap;
}
