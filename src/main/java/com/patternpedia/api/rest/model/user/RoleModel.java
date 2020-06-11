package com.patternpedia.api.rest.model.user;

import com.patternpedia.api.entities.user.role.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleModel {

    private UUID id;
    private String name;
//    private Collection<PrivilegeModel> privileges;
    private Collection<String> privileges;

    public RoleModel (Role role) {
        this.id = role.getId();
        this.name = role.getName();
//        this.privileges = role.getPrivileges().stream().map(privilege -> new PrivilegeModel(privilege)).collect(Collectors.toList());
        this.privileges = role.getPrivileges().stream().map(privilege -> privilege.getName()).collect(Collectors.toList());

    }
}
