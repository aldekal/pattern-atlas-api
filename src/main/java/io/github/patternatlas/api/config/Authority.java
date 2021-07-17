package io.github.patternatlas.api.config;

public interface Authority {
    /** ISSUE */
    String ISSUE_READ                               = "hasAuthority('ISSUE_READ')";
    String ISSUE_CREATE                             = "hasAuthority(ISSUE_CREATE')";
    String ISSUE_EDIT                               = "hasAuthority('ISSUE_EDIT')";
    String ISSUE_DELETE                             = "hasAuthority('ISSUE_DELETE')";
    String ISSUE_READ_ALL                           = "hasAuthority('ISSUE_READ_ALL')";
    String ISSUE_EDIT_ALL                           = "hasAuthority('ISSUE_EDIT_ALL')";
    String ISSUE_DELETE_ALL                         = "hasAuthority('ISSUE_DELETE_ALL')";
    String ISSUE_TO_PATTERN_CANDIDATE               = "hasAuthority('ISSUE_TO_PATTERN_CANDIDATE')";
    /** CANDIDATE */
    String PATTERN_CANDIDATE_READ                   = "hasAuthority('PATTERN_CANDIDATE_READ')";
    String PATTERN_CANDIDATE_CREATE                 = "hasAuthority('PATTERN_CANDIDATE_CREATE')";
    String PATTERN_CANDIDATE_EDIT                   = "hasAuthority('PATTERN_CANDIDATE_EDIT')";
    String PATTERN_CANDIDATE_DELETE                 = "hasAuthority('PATTERN_CANDIDATE_DELETE')";
    String PATTERN_CANDIDATE_READ_ALL               = "hasAuthority('PATTERN_CANDIDATE_READ_ALL')";
    String PATTERN_CANDIDATE_EDIT_ALL               = "hasAuthority('PATTERN_CANDIDATE_EDIT_ALL')";
    String PATTERN_CANDIDATE_DELETE_ALL             = "hasAuthority('PATTERN_CANDIDATE_DELETE_ALL')";
    String PATTERN_CANDIDATE_TO_PATTERN             = "hasAuthority('PATTERN_CANDIDATE_TO_PATTERN')";
    /** Pattern */
    String APPROVED_PATTERN_READ                    = "hasAuthority(APPROVED_PATTERN_READ')";
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
