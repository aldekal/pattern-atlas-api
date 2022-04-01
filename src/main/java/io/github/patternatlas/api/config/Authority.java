package io.github.patternatlas.api.config;

public interface Authority {

    // TODO will be phased out - after all authority checks are changed to hasResourcePermission, this file should not be
    // needed anymore

    /** CANDIDATE */
    String PATTERN_CANDIDATE_READ                   = "hasAuthority('PATTERN_CANDIDATE_READ')";
    String PATTERN_CANDIDATE_CREATE                 = "hasAuthority('PATTERN_CANDIDATE_CREATE')";
    String PATTERN_CANDIDATE_EDIT                   = "hasAuthority('PATTERN_CANDIDATE_EDIT')";
    String PATTERN_CANDIDATE_DELETE                 = "hasAuthority('PATTERN_CANDIDATE_DELETE')";
    String PATTERN_CANDIDATE_READ_ALL               = "hasAuthority('PATTERN_CANDIDATE_READ_ALL')";
    String PATTERN_CANDIDATE_EDIT_ALL               = "hasAuthority('PATTERN_CANDIDATE_EDIT_ALL')";
    String PATTERN_CANDIDATE_DELETE_ALL             = "hasAuthority('PATTERN_CANDIDATE_DELETE_ALL')";
    String PATTERN_CANDIDATE_TO_PATTERN             = "hasAuthority('PATTERN_CANDIDATE_TO_PATTERN')";
    String PATTERN_CANDIDATE_EDIT_COMBINED          = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_EDIT', #issueId))";
    String PATTERN_CANDIDATE_EDIT_DELETE          = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_DELETE', #issueId))";
    String PATTERN_CANDIDATE_EVIDENCE_COMBINED      = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_EVIDENCE', #issueId))";
    String PATTERN_CANDIDATE_VOTE_COMBINED          = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_VOTE', #issueId))";
    String PATTERN_CANDIDATE_COMMENT_COMBINED       = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_COMMENT', #issueId))";
    String PATTERN_CANDIDATE_READ_COMBINED          = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_READ', #issueId))";
    String PATTERN_CANDIDATE_READ_COMBINED_URI      = "hasAnyAuthority(@authorityService.formatResourceAuthorities('PATTERN_CANDIDATE_READ', " +
            "@issueService.getIssueByURI(#candidateUri).id))";
    /** Pattern */
    String APPROVED_PATTERN_READ                    = "hasAuthority('APPROVED_PATTERN_READ')";
    String APPROVED_PATTERN_CREATE                  = "hasAuthority('APPROVED_PATTERN_CREATE')";
    String APPROVED_PATTERN_EDIT                    = "hasAuthority('APPROVED_PATTERN_EDIT')";
    String APPROVED_PATTERN_DELETE                  = "hasAuthority('APPROVED_PATTERN_DELETE')";
    String APPROVED_PATTERN_READ_ALL                = "hasAuthority('APPROVED_PATTERN_READ_ALL')";
    String APPROVED_PATTERN_EDIT_ALL                = "hasAuthority('APPROVED_PATTERN_EDIT_ALL')";
    String APPROVED_PATTERN_DELETE_ALL              = "hasAuthority('APPROVED_PATTERN_DELETE_ALL')";
    /** USER */
    String USER_READ                                = "hasAuthority('USER_READ')";
    String USER_CREATE                              = "hasAuthority('USER_CREATE')";
    String USER_EDIT                                = "hasAuthority('USER_EDIT')";
    String USER_DELETE                              = "hasAuthority('USER_DELETE')";
    String USER_READ_ALL                            = "hasAuthority('USER_READ_ALL')";
    String USER_EDIT_ALL                            = "hasAuthority('USER_EDIT_ALL')";
    String USER_DELETE_ALL                          = "hasAuthority('USER_DELETE_ALL')";
    String USER_ALL                                 = "hasAuthority('USER_ALL')";
    /** GENERAL */
    String COMMENT                                  = "hasAuthority('COMMENT')";
    String VOTE                                     = "hasAuthority('VOTE')";
    String EVIDENCE                                 = "hasAuthority('EVIDENCE')";
}
