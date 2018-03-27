package edu.washington.cs.knowitall.regex;

import com.google.common.collect.Lists;
import org.junit.Test;

public class RegularExpressionParserTest {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        RegularExpression<String> re = RegularExpressionParsers.word.parse("<a> <b> <c>");
        Match<String> m = re.find(Lists.newArrayList("a", "b", "b", "c"));
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end-start));
        System.out.println(m.tokens());

    }
}
