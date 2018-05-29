package xlab.parsing.rule;

import xlab.parsing.tokenizer.SimpleTokenizer;
import xlab.parsing.util.RuleUtil;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RuleGeneratorTest {

    private boolean isMatch(RuleMatcher ruleMatcher, String str) {
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        str = String.join(" ", tokenizer.tokenize(str));
        return ruleMatcher.isMatch(str);
    }

    public void testRules_EN() {
        String lang = "en";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        // play music type by artist
        assertTrue(isMatch(ruleMatcher, "play a music by " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "a music by " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "play me a music by " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "play me a popular music by " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "play popular songs by " + RuleConstants.ARTIST_MAGIC_TOKEN));
        // play artist music type
        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.ARTIST_MAGIC_TOKEN + " s music"));
        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.ARTIST_MAGIC_TOKEN + " s popular music"));
//        // play artist music
//        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.ARTIST_MAGIC_TOKEN + " s music " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.ARTIST_MAGIC_TOKEN + " s " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        // play genre
//        assertTrue(isMatch(ruleMatcher, "play pop music"));
//        assertTrue(isMatch(ruleMatcher, "play the latest jazz"));
        // play artist
        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.ARTIST_MAGIC_TOKEN));
        // play music
//        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        assertTrue(isMatch(ruleMatcher, "play the song " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        // play music by artist
//        assertTrue(isMatch(ruleMatcher, "play " + RuleConstants.MUSIC_MAGIC_TOKEN + " by " + RuleConstants.ARTIST_MAGIC_TOKEN));
//        assertTrue(isMatch(ruleMatcher, "play the song " + RuleConstants.MUSIC_MAGIC_TOKEN + " by " + RuleConstants.ARTIST_MAGIC_TOKEN));
    }

    public void testRules_FR() throws Exception {
        String lang = "fr";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        // play music type by artist
        assertTrue(isMatch(ruleMatcher, "Joue le dernier album de " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "mais une musique de " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "Chanson de " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "mets nous une chanson d " + RuleConstants.ARTIST_MAGIC_TOKEN));
        // play artist
        assertTrue(isMatch(ruleMatcher, "Écoute " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "mets du " + RuleConstants.ARTIST_MAGIC_TOKEN));
//        // play music
//        assertTrue(isMatch(ruleMatcher, "joue-nous un " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        assertTrue(isMatch(ruleMatcher, "mets la musique " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        // play music by artist
//        assertTrue(isMatch(ruleMatcher, "lance la musique " + RuleConstants.MUSIC_MAGIC_TOKEN + " de " + RuleConstants.ARTIST_MAGIC_TOKEN));
    }

    public void testRules_DE() throws Exception {
        String lang = "de";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        // play music type by artist
        assertTrue(isMatch(ruleMatcher, "Spiele Musik von " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "Spiel was von " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "Bitte ein Song von " + RuleConstants.ARTIST_MAGIC_TOKEN));
        assertTrue(isMatch(ruleMatcher, "Spiel mir ein Lied von " + RuleConstants.ARTIST_MAGIC_TOKEN));
        // play artist
        assertTrue(isMatch(ruleMatcher, "spiele " + RuleConstants.ARTIST_MAGIC_TOKEN));
//        // play music
//        assertTrue(isMatch(ruleMatcher, "spiele " + RuleConstants.MUSIC_MAGIC_TOKEN));
//        // play music by artist
//        assertTrue(isMatch(ruleMatcher, "spiele die " + RuleConstants.MUSIC_MAGIC_TOKEN + " von " + RuleConstants.ARTIST_MAGIC_TOKEN));
    }

    public void testRules_ZH() throws Exception {
        String lang = "zh";
        RuleUtil.buildRuleMatcherMap(Sets.newHashSet(lang));
        RuleMatcher ruleMatcher = RuleUtil.getRuleMatcher(lang);
        // play music type by artist
        assertTrue(isMatch(ruleMatcher, "来一首" + RuleConstants.ARTIST_MAGIC_TOKEN + "的最新歌曲"));
        assertTrue(isMatch(ruleMatcher, "放歌手" + RuleConstants.ARTIST_MAGIC_TOKEN + "的专辑"));
        assertTrue(isMatch(ruleMatcher, "放" + RuleConstants.ARTIST_MAGIC_TOKEN + "的专辑"));
        // play artist music type
        assertTrue(isMatch(ruleMatcher, "放" + RuleConstants.ARTIST_MAGIC_TOKEN + "的一首新歌"));
//        // play music
//        assertTrue(isMatch(ruleMatcher, "放" + RuleConstants.MUSIC_MAGIC_TOKEN));
//        assertTrue(isMatch(ruleMatcher, "来一首" + RuleConstants.MUSIC_MAGIC_TOKEN));
//        // play music by artist
//        assertTrue(isMatch(ruleMatcher, "放" + RuleConstants.ARTIST_MAGIC_TOKEN + "的" + RuleConstants.MUSIC_MAGIC_TOKEN));
    }
}
