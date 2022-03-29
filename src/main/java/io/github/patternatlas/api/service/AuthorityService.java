package io.github.patternatlas.api.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service("authorityService")
public class AuthorityService {



    /**
     * @param baseAuthority base authority e.g. ISSUE_EDIT
     * @param resource Resource for creating resource specific authority
     * @return List of authorities allowing to perform action on resource (e.g. ISSUE_EDIT_ALL, ISSUE_EDIT_[uuid])
     */
    public List<String> formatResourceAuthorities(String baseAuthority, UUID resource){
        List<String> authorities = Arrays.asList(baseAuthority + "_ALL", baseAuthority + "_" + resource.toString());
        return authorities;
    }
}
