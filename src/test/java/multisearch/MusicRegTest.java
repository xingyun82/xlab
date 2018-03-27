package multisearch;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicRegTest {

//    @Test
//    public void testPattern() {
//        Beats1QueryParser query = new Beats1QueryParser();
//        long start = System.currentTimeMillis();
//        String showName = "rocket hour";
//        System.out.println(query.match(showName));
//        System.out.println(query.match(showName + " station"));
//        System.out.println(query.match(showName + " damb"));
//        System.out.println(query.match(showName  + " on beats 1"));
//        System.out.println(query.match(showName + " station on beats 1"));
//        System.out.println(query.match(showName + " with kesha"));
//        System.out.println(query.match(showName + " station with kesha"));
//        System.out.println(query.match(showName + " with kesha on beats 1"));
//        System.out.println(query.match("elton john's " + showName));
//        System.out.println(query.match("elton john's " + showName + " station"));
//        System.out.println(query.match("elton john's " + showName + " on beats 1"));
//        System.out.println(query.match("elton john's " + showName + " with kesha on beats 1"));
//
//        System.out.println(query.match("an episode of " + showName));
//        System.out.println(query.match("new episode of " + showName));
//        System.out.println(query.match("newest " + showName));
//        System.out.println(query.match("the new " + showName));
//        System.out.println(query.match("latest " + showName));
//        System.out.println(query.match("the newest " + showName));
//        System.out.println(query.match("latest episode of " + showName));
//        System.out.println(query.match("latest episode of " + showName + " on beats 1"));
//        System.out.println(query.match("the latest episode of " + showName));
//        System.out.println(query.match("the latest episode of " + showName + " on beats 1"));
//        System.out.println(query.match("the latest episode of " + showName + " with kesha"));
//        System.out.println(query.match("the latest episode of " + showName + " with kesha on beats 1"));
//        System.out.println(query.match("the latest episode of elton john's " + showName + " on beats 1"));
//        System.out.println(query.match("the latest episode of elton john's "+ showName + " with kesha"));
//        System.out.println(query.match("the latest episode of elton john's " + showName + " with kesha on beats 1"));
//
//        Beats1QueryParsingResult result = query.parsing("the latest episode of elton john's " + showName + " with kesha on beats 1");
//        System.out.println(result.getHostName());
//        System.out.println(result.getWithSomeone());
//        System.out.println(result.getShowName());
//        long end = System.currentTimeMillis();
//        System.out.println("time:" + (end-start));
//    }
//
//    @Test
//    public void testPattern2() {
//        String REGEX_HOST_NAME = "(?<hostName>.+[^'s])('s)? ";
//        Pattern p = Pattern.compile(REGEX_HOST_NAME);
//        Matcher m = p.matcher("elton john's ");
//        if(m.find()) {
//            System.out.println(m.group("hostName"));
//        }
//    }
//
//    @Test
//    public void test() {
//        Beats1QueryParser query = new Beats1QueryParser();
//        String pattern = "((the )?(latest|newest|new) (episode of )?|(an|new) episode of )?((?<hostName>.+[^'s])('s)? )?BEATS1_SHOW_NAME( station)?( with (?<withSomeone>.+[^ on beats 1]))?( on beats (1|one))?";
//        Beats1QueryParsingResult result = query.parsing("heloagag rocket hour asgasgas");
//        if (result != null) {
//            System.out.println(result.getHostName());
//            System.out.println(result.getWithSomeone());
//            System.out.println(result.getShowName());
//            long end = System.currentTimeMillis();
//        }
//    }
//
//    @Test
//    public void testPlayArtistMusic() {
//
//    }
}
