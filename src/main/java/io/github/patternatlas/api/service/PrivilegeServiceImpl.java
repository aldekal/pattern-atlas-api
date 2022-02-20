package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.repositories.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.patternatlas.api.service.PrivilegeService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

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
    public void deleteAllPrivilegesByResourceId(UUID resourceId) {
        this.privilegeRepository.deleteAllByResourceId(resourceId);
    }

}
