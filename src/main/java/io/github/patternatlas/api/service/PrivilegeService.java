package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.user.role.Privilege;

import java.util.List;
import java.util.UUID;

public interface PrivilegeService {
    /** CRUD  */
    Privilege createPrivilege(String name);

    void deleteAllPrivilegesByResourceId(UUID entityId);
}
