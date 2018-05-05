package xlab.parsing.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MusicTypeUtilTest {

    @Test
    public void test() {
        assertEquals("music", MusicTypeUtil.getStandardName("music", "en"));
        assertEquals(null, MusicTypeUtil.getStandardName("radio", "en"));
        assertEquals(null, MusicTypeUtil.getStandardName("podcast", "en"));
        assertEquals(null, MusicTypeUtil.getStandardName("station", "en"));
        assertEquals(null, MusicTypeUtil.getStandardName("song", "xx"));
    }
}
