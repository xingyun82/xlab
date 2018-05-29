package xlab.parsing.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterUtilTest {

    @Test
    public void testIsChineseChar() {
        assertTrue(CharacterUtil.isChineseChar('汉'));
        assertFalse(CharacterUtil.isChineseChar('h'));
    }

    @Test
    public void testIsJapaneseChar() {
        assertTrue(CharacterUtil.isJapneseChar('な'));
        assertTrue(CharacterUtil.isJapneseChar('タ'));
    }
}
