package com.lrs.common.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;

import static org.mockito.Matchers.anyInt;

@RunWith(PowerMockRunner.class)
public class ToolsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @PrepareForTest({Random.class, Tools.class})
    @Test
    public void testRandom1() throws Exception {
        Random random = PowerMockito.mock(Random.class);
        PowerMockito.when(random.nextInt(anyInt())).thenReturn(2);
        PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);

        Assert.assertEquals("2", Tools.random(1));
    }

    @PrepareForTest({Random.class, Tools.class})
    @Test
    public void testRandom2() throws Exception {
        Random random = PowerMockito.mock(Random.class);
        PowerMockito.when(random.nextInt(anyInt())).thenReturn(2).thenReturn(3);
        PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);

        Assert.assertEquals("23", Tools.random(2));
    }

    @Test
    public void testRandomThrowsException() {
        thrown.expect(IllegalArgumentException.class);
        Tools.random(0);
        // Method is not expected to return due to exception thrown
    }

    @PrepareForTest({Random.class, Tools.class})
    @Test
    public void testGetRandomNum() throws Exception {
        Random random = PowerMockito.mock(Random.class);
        PowerMockito.when(random.nextInt(anyInt())).thenReturn(3);
        PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);

        Assert.assertEquals(4, Tools.getRandomNum(1, 5));
    }

    @Test
    public void testNotEmpty() {
        Assert.assertFalse(Tools.notEmpty(""));
        Assert.assertFalse(Tools.notEmpty("null"));
        Assert.assertFalse(Tools.notEmpty(null));

        Assert.assertTrue(Tools.notEmpty(" "));
        Assert.assertTrue(Tools.notEmpty("foo"));
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(Tools.isEmpty(" "));
        Assert.assertFalse(Tools.isEmpty("foo"));

        Assert.assertTrue(Tools.isEmpty(""));
        Assert.assertTrue(Tools.isEmpty("null"));
        Assert.assertTrue(Tools.isEmpty(null));
    }

    @Test
    public void testIsNumber() {
        Assert.assertTrue(Tools.isNumber("3"));

        Assert.assertFalse(Tools.isNumber("a"));
        Assert.assertFalse(Tools.isNumber("%"));
    }
}
