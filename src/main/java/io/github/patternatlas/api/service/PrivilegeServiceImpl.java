package io.github.patternatlas.api.service;

import java.util.UUID;

import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.repositories.PrivilegeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

    private PrivilegeRepository privilegeRepository;

    Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

    public PrivilegeServiceImpl(
            PrivilegeRepository privilegeRepository
    ) {
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * CRUD Privilege
     */
    @Override
    @Transactional
    public Privilege createPrivilege(String name) {
        Privilege privilege = new Privilege(name);

        return this.privilegeRepository.save(privilege);
    }

    @Override
    @Transactional
    public void deleteAllPrivilegesByResourceId(UUID entityId) {
        this.privilegeRepository.deleteAllFromEntity(entityId);
    }

}
