package io.github.patternatlas.api.rest.model.user;

import io.github.patternatlas.api.entities.user.role.Privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PrivilegeModel {

    private UUID id;

    private String name;

    public PrivilegeModel(Privilege privilege) {
        this.id = privilege.getId();
        this.name = privilege.getName();
    }
}
