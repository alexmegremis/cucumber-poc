package com.alexmegremis.funfun.api;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.EnumSet;

public class BitwiseTest {

    enum PERMISSIONS {
        READ, WRITE, ADMIN, CREATE_REPO, DELETE_REPO, CREATE_BRANCH, DELETE_BRANCH, COMMENT, CREATE_TAG, CLONE
    }

    public static void test() {

        EnumSet<PERMISSIONS> permissions = EnumSet.of(PERMISSIONS.READ, PERMISSIONS.CREATE_BRANCH, PERMISSIONS.COMMENT);

        Integer sum = permissions.stream().map(PERMISSIONS :: ordinal).reduce(0, (a, b) -> a |= (1 << b));
        System.out.println(">>> " + sum);

        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.READ)));
        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.CREATE_BRANCH)));
        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.COMMENT)));
        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.CREATE_BRANCH, PERMISSIONS.COMMENT)));
        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.CREATE_REPO, PERMISSIONS.COMMENT)));
        Assert.isTrue(!hasPermission(sum, getPermissionSum(PERMISSIONS.WRITE, PERMISSIONS.CREATE_REPO)));
        Assert.isTrue(hasPermission(sum, getPermissionSum(PERMISSIONS.WRITE, PERMISSIONS.CREATE_BRANCH)));
        Assert.isTrue(!hasPermission(sum, getPermissionSum(PERMISSIONS.WRITE)));
        Assert.isTrue(!hasPermission(sum, getPermissionSum(PERMISSIONS.ADMIN)));
    }

    static boolean hasPermission(final Integer sum, final Integer test) {
        return (sum & test) > 0;
    }

    static Integer getPermissionSum(final PERMISSIONS... permissionTest) {
        Integer result = Arrays.stream(permissionTest).map(PERMISSIONS :: ordinal).reduce(0, (a, b) -> a |= (1 << b));
        return result;
    }
}
