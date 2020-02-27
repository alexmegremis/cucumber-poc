package com.alexmegremis.funfun.api;

import java.util.EnumSet;

public class BitwiseTest {

    public static final EnumSet<PERMISSIONS> P_WRITE        = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CLONE, PERMISSIONS.WRITE, PERMISSIONS.CREATE_BRANCH, PERMISSIONS.COMMENT,
                                                                         PERMISSIONS.DELETE_BRANCH, PERMISSIONS.CREATE_REPO, PERMISSIONS.CREATE_TAG);
    public static final EnumSet<PERMISSIONS> P_WRITE_SIMPLE = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CLONE, PERMISSIONS.WRITE);
    public static final EnumSet<PERMISSIONS> P_WRITE_MEDIUM = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CLONE, PERMISSIONS.WRITE, PERMISSIONS.COMMENT);
    public static final EnumSet<PERMISSIONS> P_MIXED        = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CLONE, PERMISSIONS.CREATE_BRANCH, PERMISSIONS.CREATE_TAG,
                                                                         PERMISSIONS.COMMENT);
    public static final EnumSet<PERMISSIONS> P_READ         = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CLONE);

    public enum PERMISSIONS {
        READ, WRITE, ADMIN, CREATE_REPO, DELETE_REPO, CREATE_BRANCH, DELETE_BRANCH, COMMENT, CREATE_TAG, CLONE
    }

    public static void test() {

        EnumSet<PERMISSIONS> permissions = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CREATE_BRANCH, PERMISSIONS.COMMENT);

        Integer sum = getPermission(permissions);
        System.out.println(">>> " + sum);

        System.out.println(hasPermission(sum, PERMISSIONS.READ));
        System.out.println(hasPermission(sum, PERMISSIONS.CREATE_BRANCH));
        System.out.println(hasPermission(sum, PERMISSIONS.COMMENT));
        System.out.println(hasPermission(sum, PERMISSIONS.WRITE));
        System.out.println(hasPermission(sum, PERMISSIONS.ADMIN));
    }

    public static boolean hasPermission(final Integer sum, final PERMISSIONS permission) {
        return (sum & 1 << permission.ordinal()) > 0;
    }

    public static Integer getPermission(EnumSet<PERMISSIONS> permissions) {
        Integer result = permissions.stream().map(PERMISSIONS :: ordinal).reduce(0, (a, b) -> a |= (1 << b));
        return result;
    }
}
