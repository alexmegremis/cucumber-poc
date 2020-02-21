package com.alexmegremis.funfun.api;

import java.util.EnumSet;

public class BitwiseTest {

    enum PERMISSIONS {
        READ, WRITE, ADMIN, CREATE_REPO, DELETE_REPO, CREATE_BRANCH, DELETE_BRANCH, COMMENT, CREATE_TAG, CLONE
    }

    public static void test() {

        EnumSet<PERMISSIONS> permissions = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CREATE_BRANCH, PERMISSIONS.COMMENT);

        Integer sum = permissions.stream().map(PERMISSIONS :: ordinal).reduce(0, (a, b) -> a |= (1 << b));
        System.out.println(">>> " + sum);

        System.out.println(hasPermission(sum, PERMISSIONS.READ));
        System.out.println(hasPermission(sum, PERMISSIONS.CREATE_BRANCH));
        System.out.println(hasPermission(sum, PERMISSIONS.COMMENT));
        System.out.println(hasPermission(sum, PERMISSIONS.WRITE));
        System.out.println(hasPermission(sum, PERMISSIONS.ADMIN));
    }

    static boolean hasPermission(final Integer sum, final PERMISSIONS permission) {
        return (sum & 1 << permission.ordinal()) > 0;
    }
}
