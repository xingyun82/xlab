package multisearch;

import com.apple.EnPatterns;
import com.apple.GroupName;
import com.apple.NameEntityTag;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RE2Test {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        Matcher m = EnPatterns.EN_PATTERN_PLAY_RADIO_SHOW.matcher("rocket man by " + NameEntityTag.BEATS1_SHOW_NAME_TAG);
        boolean find = m.find();
        if (find) {
            System.out.println(find);
            System.out.println(m.group(GroupName.GROUP_HOST_NAME));
            System.out.println(m.group(GroupName.GROUP_WITH_SOMEONE));
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }


    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        Matcher m = EnPatterns.EN_PATTERN_PLAY_RADIO_SHOW.matcher("the latest episode elton john's " + NameEntityTag.BEATS1_SHOW_NAME_TAG + " with kesha");
        boolean find = m.find();
        if (find) {
            System.out.println(find);
            System.out.println(m.group(GroupName.GROUP_HOST_NAME));
            System.out.println(m.group(GroupName.GROUP_WITH_SOMEONE));
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }

    @Test
    public void test2() {
        long start = System.currentTimeMillis();
        Matcher m = EnPatterns.EN_PATTERN_PLAY_RADIO_SHOW.matcher("elton john's " + NameEntityTag.BEATS1_SHOW_NAME_TAG + " featuring kesha");
        boolean find = m.find();
        if (find) {
            System.out.println(find);
            System.out.println(m.group(GroupName.GROUP_HOST_NAME));
            System.out.println(m.group(GroupName.GROUP_WITH_SOMEONE));
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }


    @Test
    public void test3() {
        long start = System.currentTimeMillis();
        Matcher m = EnPatterns.EN_PATTERN_PLAY_RADIO_SHOW.matcher("elton john's " + NameEntityTag.BEATS1_SHOW_NAME_TAG + " with guest kesha");
        boolean find = m.find();
        if (find) {
            System.out.println(find);
            System.out.println(m.group(GroupName.GROUP_HOST_NAME));
            System.out.println(m.group(GroupName.GROUP_WITH_SOMEONE));
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }


}
