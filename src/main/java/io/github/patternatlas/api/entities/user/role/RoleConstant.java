package io.github.patternatlas.api.entities.user.role;

import java.util.Arrays;
import java.util.List;

public class RoleConstant {
    public static final String MEMBER = "MEMBER";
    public static final String HELPER = "HELPER";
    public static final String MAINTAINER = "MAINTAINER";
    public static final String OWNER = "OWNER";
    public static final String EXPERT = "EXPERT";
    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String ADMIN = "ADMIN";
    public static final String DEVELOPER = "DEVELOPER";
    public static final String GUEST = "GUEST";

    public static List<String> PLATFORM_ROLES = Arrays.asList(
                RoleConstant.ADMIN,
                RoleConstant.MEMBER,
                RoleConstant.EXPERT,
                RoleConstant.LIBRARIAN,
                RoleConstant.GUEST
    );

    public static List<String> AUTHOR_ROLES = Arrays.asList(
                RoleConstant.HELPER,
                RoleConstant.MAINTAINER,
                RoleConstant.OWNER
    );

}

