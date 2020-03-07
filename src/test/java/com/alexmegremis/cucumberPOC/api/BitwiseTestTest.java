package com.alexmegremis.cucumberPOC.api;

import org.junit.Test;
import com.alexmegremis.cucumberPOC.api.BitwiseTest.PERMISSIONS;
import static com.alexmegremis.cucumberPOC.api.BitwiseTest.PERMISSIONS.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

public class BitwiseTestTest {

    EnumSet<PERMISSIONS> onlyReads = EnumSet.of(READ, COMMENT, CLONE);
    EnumSet<PERMISSIONS> mixed = EnumSet.of(WRITE, CLONE, CREATE_BRANCH, READ, COMMENT);

    @Test
    public void hasPermission_fromOnlyReads_false() {
        final Integer permission = BitwiseTest.getPermission(onlyReads);
        assertFalse(BitwiseTest.hasPermission(permission, WRITE));
        assertFalse(BitwiseTest.hasPermission(permission, CREATE_BRANCH));
        assertFalse(BitwiseTest.hasPermission(permission, DELETE_REPO));
    }

    @Test
    public void hasPermission_fromOnlyReads_true() {
        final Integer permission = BitwiseTest.getPermission(onlyReads);
        assertTrue(BitwiseTest.hasPermission(permission, READ));
        assertTrue(BitwiseTest.hasPermission(permission, COMMENT));
    }
    @Test
    public void hasPermission_fromMixed_false() {
        final Integer permission = BitwiseTest.getPermission(mixed);
        assertFalse(BitwiseTest.hasPermission(permission, DELETE_BRANCH));
        assertFalse(BitwiseTest.hasPermission(permission, DELETE_REPO));
        assertFalse(BitwiseTest.hasPermission(permission, ADMIN));
    }

    @Test
    public void hasPermission_fromMixed_true() {
        final Integer permission = BitwiseTest.getPermission(mixed);
        assertTrue(BitwiseTest.hasPermission(permission, WRITE));
        assertTrue(BitwiseTest.hasPermission(permission, CREATE_BRANCH));
        assertTrue(BitwiseTest.hasPermission(permission, READ));
    }
}