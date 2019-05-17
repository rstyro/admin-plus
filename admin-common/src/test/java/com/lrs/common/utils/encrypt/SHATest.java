package com.lrs.common.utils.encrypt;

import org.junit.Assert;
import org.junit.Test;
import java.security.NoSuchAlgorithmException;

public class SHATest {

    private static final String SHA_foo =
            "0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33";
    private static final String SHA_1_foo =
            "0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33";
    private static final String SHA_256_foo =
            "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae";
    private static final String SHA_bar =
            "62cdb7020ff920e5aa642c3d4066950dd1f01f4d";
    private static final String SHA_1_bar =
            "62cdb7020ff920e5aa642c3d4066950dd1f01f4d";
    private static final String SHA_256_bar =
            "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9";

    @Test
    public void testEncryptSHA() throws Exception {
        final byte[] data = {112, 55, -128, 113, -104, -62, 42, 125, 43, 8,
                7, 55, 29, 118, 55, 121, -88, 79, -33, -49};
        Assert.assertArrayEquals(data, SHA.encryptSHA(new byte[]{1, 2, 3}));
    }

    @Test
    public void testEncryptSHAString() throws Exception {
        Assert.assertEquals("", SHA.encryptSHA((String) null));
        Assert.assertEquals(SHA_foo, SHA.encryptSHA("foo"));
        Assert.assertEquals(SHA_bar, SHA.encryptSHA("bar"));
    }

    @Test
    public void testEncryptSHAStringString() throws NoSuchAlgorithmException {
        Assert.assertEquals("", SHA.encryptSHA(null, null));
        Assert.assertEquals(SHA_foo, SHA.encryptSHA("foo", "SHA"));
        Assert.assertEquals(SHA_1_foo, SHA.encryptSHA("foo", "SHA-1"));
        Assert.assertEquals(SHA_256_foo, SHA.encryptSHA("foo", "SHA-256"));
        Assert.assertEquals(SHA_bar, SHA.encryptSHA("bar", "SHA"));
        Assert.assertEquals(SHA_1_bar, SHA.encryptSHA("bar", "SHA-1"));
        Assert.assertEquals(SHA_256_bar, SHA.encryptSHA("bar", "SHA-256"));
    }
}
