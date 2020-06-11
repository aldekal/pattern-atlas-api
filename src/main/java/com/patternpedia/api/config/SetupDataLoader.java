package com.patternpedia.api.config;

import com.patternpedia.api.entities.user.role.PrivilegeConstant;
import com.patternpedia.api.entities.user.role.RoleConstant;
import com.patternpedia.api.entities.user.role.Privilege;
import com.patternpedia.api.entities.user.role.Role;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.PrivilegeRepository;
import com.patternpedia.api.repositories.RoleRepository;
import com.patternpedia.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;

        /** Privileges */
        /** USER */
        Privilege readUserAllPrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.READ_USER_ALL);
        Privilege readUserPrivilege     = createPrivilegeIfNotFound(PrivilegeConstant.READ_USER);
        Privilege createUserPrivilege   = createPrivilegeIfNotFound(PrivilegeConstant.CREATE_USER);
        Privilege updateUserPrivilege   = createPrivilegeIfNotFound(PrivilegeConstant.UPDATE_USER);
        Privilege deleteUserPrivilege   = createPrivilegeIfNotFound(PrivilegeConstant.DELETE_USER);
        /** ISSUE */
        Privilege readIssuePrivilege    = createPrivilegeIfNotFound(PrivilegeConstant.READ_ISSUE);
        Privilege createIssuePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.CREATE_ISSUE);
        Privilege updateIssuePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.UPDATE_ISSUE);
        Privilege deleteIssuePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.DELETE_ISSUE);
        /** CANDIDATE */
        Privilege readCandidatePrivilege    = createPrivilegeIfNotFound(PrivilegeConstant.READ_CANDIDATE);
        Privilege createCandidatePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.CREATE_CANDIDATE);
        Privilege updateCandidatePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.UPDATE_CANDIDATE);
        Privilege deleteCandidatePrivilege  = createPrivilegeIfNotFound(PrivilegeConstant.DELETE_CANDIDATE);
        /** ROLE */
        String UPDATE_ROLE          = "UPDATE_ROLE";
        /** DEVELOPER */
        Privilege developerPrivilege        = createPrivilegeIfNotFound(PrivilegeConstant.DEVELOPER);

        /** Roles */
        createRoleIfNotFound(RoleConstant.MEMBER, Arrays.asList(
                //USER
                updateUserPrivilege, readUserPrivilege,
                //ISSUES
                readIssuePrivilege, createIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege
        ));
        createRoleIfNotFound(RoleConstant.EXPERT, Arrays.asList(
                //USER
                updateUserPrivilege,
                //ISSUES
                readIssuePrivilege, createIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege,
                //CANDIDATE
                createCandidatePrivilege, updateCandidatePrivilege, deleteCandidatePrivilege
        ));
        createRoleIfNotFound(RoleConstant.LIBRARIAN, Arrays.asList(
                //USER
                readUserAllPrivilege, readUserPrivilege, createUserPrivilege, updateUserPrivilege, deleteUserPrivilege,
                //ISSUES
                readIssuePrivilege, createIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege,
                //CANDIDATE
                createCandidatePrivilege, updateCandidatePrivilege, deleteCandidatePrivilege
        ));
        createRoleIfNotFound(RoleConstant.ADMIN, Arrays.asList(
                //USER
                readUserAllPrivilege, readUserPrivilege, createUserPrivilege, updateUserPrivilege, deleteUserPrivilege,
                //ISSUES
                readIssuePrivilege, createIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege,
                //CANDIDATE
                createCandidatePrivilege, updateCandidatePrivilege, deleteCandidatePrivilege,
                //DEVELOPER
                developerPrivilege
        ));
        createRoleIfNotFound(RoleConstant.DEVELOPER, Arrays.asList(
                //USER
                readUserAllPrivilege, readUserPrivilege, createUserPrivilege, updateUserPrivilege, deleteUserPrivilege,
                //ISSUES
                readIssuePrivilege, createIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege,
                //CANDIDATE
                createCandidatePrivilege, updateCandidatePrivilege, deleteCandidatePrivilege,
                //DEVELOPER
                developerPrivilege
        ));

        createUser(new UserEntity("MEMBER", "member@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.MEMBER)));
        createUser(new UserEntity("MEMBER_1", "member1@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.MEMBER)));
        createUser(new UserEntity("EXPERT", "expert@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.EXPERT)));
        createUser(new UserEntity("LIBRARIAN", "librarian@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.LIBRARIAN)));
        createUser(new UserEntity("ADMIN", "admin@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.ADMIN)));
        createUser(new UserEntity("DEVELOPER", "developer@mail", passwordEncoder.encode("pass"), roleRepository.findByName(RoleConstant.DEVELOPER)));



        alreadySetup = true;
    }

    @Transactional
    void createUser(UserEntity userEntity) {
        UserEntity user = userRepository.findByEmail(userEntity.getEmail());
        if (user == null)
            userRepository.save(userEntity);
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


}
