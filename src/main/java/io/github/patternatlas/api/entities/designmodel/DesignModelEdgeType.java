package io.github.patternatlas.api.entities.designmodel;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DesignModelEdgeType {

    @Id
    private String name;

    private Boolean swap;
}
