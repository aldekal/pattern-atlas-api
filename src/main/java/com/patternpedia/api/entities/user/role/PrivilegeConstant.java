package com.patternpedia.api.entities.user.role;

public interface PrivilegeConstant {
    /** USER */
    String READ_USER_ALL        = "READ_USER_ALL";
    String READ_USER            = "READ_USER";
    String CREATE_USER          = "CREATE_USER";
    String UPDATE_USER          = "UPDATE_USER";
    String DELETE_USER          = "DELETE_USER";
    /** ISSUE */
    String READ_ISSUE           = "READ_ISSUE";
    String CREATE_ISSUE         = "CREATE_ISSUE";
    String UPDATE_ISSUE         = "UPDATE_ISSUE";
    String DELETE_ISSUE         = "DELETE_ISSUE";
    /** CANDIDATE */
    String READ_CANDIDATE       = "READ_CANDIDATE";
    String CREATE_CANDIDATE     = "CREATE_CANDIDATE";
    String UPDATE_CANDIDATE     = "UPDATE_CANDIDATE";
    String DELETE_CANDIDATE     = "DELETE_CANDIDATE";
    /** ROLE */
    String UPDATE_ROLE          = "UPDATE_ROLE";
    /** DEVELOPER */
    String DEVELOPER            = "DEVELOPER";
}
