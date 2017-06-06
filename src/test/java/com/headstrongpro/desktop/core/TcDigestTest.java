package com.headstrongpro.desktop.core;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.30.
 */
public class TcDigestTest {
    private static TcDigest tcDigest;
    private final String text = "She sells sea shells on a sea shore.";

    @BeforeClass
    public static void setUp() throws Exception {
        tcDigest = new TcDigest();
    }

    @Test
    public void encode() throws Exception {
        String computed = tcDigest.encode(text);
        System.out.printf("Computed value: %s%n", computed);

        assertNotEquals(text, computed);
    }

    @Test
    public void integrityCheck1() throws Exception {
        String computed = tcDigest.encode(text);
        Method m = tcDigest.getClass().getDeclaredMethod("decode", String.class);
        m.setAccessible(true);
        String decoded = (String) m.invoke(tcDigest, computed);
        System.out.printf("Computed value: %s%n", computed);
        System.out.printf("Decoded value: %s%n", decoded);

        assertEquals(text, decoded);
    }

    @Test
    public void customEncodingHEX() throws Exception {
        String seed = "custom seed";
        TcDigest.DigestSystem system = TcDigest.DigestSystem.HEX;

        String computed = tcDigest.encode(text, seed, system);
        String decoded = tcDigest.decode(computed, seed, system);
        System.out.printf("Computed value: %s%n", computed);
        System.out.printf("Decoded value: %s%n", decoded);

        assertEquals(text, decoded);
    }

}