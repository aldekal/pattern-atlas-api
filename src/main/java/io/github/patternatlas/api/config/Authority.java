package io.github.patternatlas.api.config;

public interface Authority {

    // TODO will be phased out - after all authority checks are changed to hasResourcePermission, this file should not be
    // needed anymore

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
