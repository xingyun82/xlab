package xlab.parsing.util;

import xlab.parsing.rule.RuleMatcher;
import com.github.jcrfsuite.util.Pair;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TagResultUtilTest {

    public void testGetSlotsFromTagResult_EN() {
        TagResultUtil tagResultUtil = new TagResultUtil();
        List<Pair<String, String>> tokenPairs = new ArrayList<>();
        tokenPairs.add(new Pair<>("play", "O"));
        tokenPairs.add(new Pair<>("shake", "MUSIC-B"));
        tokenPairs.add(new Pair<>("it", "MUSIC-I"));
        tokenPairs.add(new Pair<>("off", "MUSIC-I"));
        tokenPairs.add(new Pair<>("by", "O"));
        tokenPairs.add(new Pair<>("taylor", "ARTIST-B"));
        tokenPairs.add(new Pair<>("swift", "ARTIST-I"));
        List<Pair<String, String>> slots = tagResultUtil.getSlotsFromTagResult(tokenPairs);
        assertEquals(4, slots.size());
        assertEquals("O", slots.get(0).getFirst());
        assertEquals("play", slots.get(0).getSecond());
        assertEquals("MUSIC", slots.get(1).getFirst());
        assertEquals("shake it off", slots.get(1).getSecond());
        assertEquals("O", slots.get(2).getFirst());
        assertEquals("by", slots.get(2).getSecond());
        assertEquals("ARTIST", slots.get(3).getFirst());
        assertEquals("taylor swift", slots.get(3).getSecond());
    }

    public void testGetSlotsFromTagResult_ZH() {
        TagResultUtil tagResultUtil = new TagResultUtil();
        List<Pair<String, String>> tokenPairs = new ArrayList<>();
        tokenPairs.add(new Pair<>("放", "O"));
        tokenPairs.add(new Pair<>("张", "ARTIST-B"));
        tokenPairs.add(new Pair<>("学", "ARTIST-I"));
        tokenPairs.add(new Pair<>("友", "ARTIST-I"));
        tokenPairs.add(new Pair<>("的", "O"));
        tokenPairs.add(new Pair<>("吻", "MUSIC-B"));
        tokenPairs.add(new Pair<>("别", "MUSIC-I"));
        List<Pair<String, String>> slots = tagResultUtil.getSlotsFromTagResult(tokenPairs);
        assertEquals(4, slots.size());
        assertEquals("O", slots.get(0).getFirst());
        assertEquals("放", slots.get(0).getSecond());
        assertEquals("ARTIST", slots.get(1).getFirst());
        assertEquals("张学友", slots.get(1).getSecond());
        assertEquals("O", slots.get(2).getFirst());
        assertEquals("的", slots.get(2).getSecond());
        assertEquals("MUSIC", slots.get(3).getFirst());
        assertEquals("吻别", slots.get(3).getSecond());
    }

    public void testMatchRule_EN() {
        String lang = "en";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        TagResultUtil tagResultUtil = new TagResultUtil();
        List<Pair<String, String>> tokenPairs = new ArrayList<>();
        tokenPairs.add(new Pair<>("play", "O"));
        tokenPairs.add(new Pair<>("songs", "MUSIC_TYPE-B"));
        tokenPairs.add(new Pair<>("by", "O"));
        tokenPairs.add(new Pair<>("taylor", "ARTIST-B"));
        tokenPairs.add(new Pair<>("swift", "ARTIST-I"));
        List<Pair<String, String>> slots = tagResultUtil.getSlotsFromTagResult(tokenPairs);
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        assertTrue(tagResultUtil.matchRule(ruleMatcher, slots));
    }

    public void testMatchRule_ZH() {
        String lang = "zh";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        TagResultUtil tagResultUtil = new TagResultUtil();
        List<Pair<String, String>> tokenPairs = new ArrayList<>();
        tokenPairs.add(new Pair<>("放", "O"));
        tokenPairs.add(new Pair<>("张", "ARTIST-B"));
        tokenPairs.add(new Pair<>("学", "ARTIST-I"));
        tokenPairs.add(new Pair<>("友", "ARTIST-I"));
        tokenPairs.add(new Pair<>("的", "O"));
        tokenPairs.add(new Pair<>("专", "MUSIC_TYPE-B"));
        tokenPairs.add(new Pair<>("辑", "MUSIC_TYPE-I"));
        List<Pair<String, String>> slots = tagResultUtil.getSlotsFromTagResult(tokenPairs);
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        assertTrue(tagResultUtil.matchRule(ruleMatcher, slots));
    }
}
