package xlab.parsing.util;

public class CharacterUtil {

    public static boolean isChineseChar(char c) {
        return (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN);
    }

    public static boolean isJapneseChar(char c) {
        Character.UnicodeScript script = Character.UnicodeScript.of(c);
        return script == Character.UnicodeScript.KATAKANA || script == Character.UnicodeScript.HIRAGANA;
    }
}
