package xlab.parsing.util;


import xlab.parsing.tokenizer.SimpleTokenizer;
import xlab.parsing.tokenizer.TrainDataTokenizer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleStringTokenizerTest {

    @Test
    public void testTrainTokenizeDelimeters() {
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize("ag~`!@#$%^&*()_-+={[]}\\|:;\"'<,>?/gaag");
        assertEquals(5, tokens.size());
        assertEquals("ag", tokens.get(0));
        assertEquals("_", tokens.get(1));
        assertEquals("<", tokens.get(2));
        assertEquals(">", tokens.get(3));
        assertEquals("/gaag", tokens.get(4));
    }

    @Test
    public void testTrainTokenizePureText() {
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize("ab  bc  cd ");
        assertEquals(3, tokens.size());
        assertEquals("ab", tokens.get(0));
        assertEquals("bc", tokens.get(1));
        assertEquals("cd", tokens.get(2));
    }

    @Test
    public void testTrainTokenizeTextWithTag() {
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize("ab  <ARTIST>bc</ARTIST>  cd ");
        assertEquals(3, tokens.size());
        assertEquals("ab", tokens.get(0));
        assertEquals("<ARTIST>bc</ARTIST>", tokens.get(1));
        assertEquals("cd", tokens.get(2));
    }

    @Test
    public void testTrainTokenizeTextWithTag2() {
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize("ab  <MUSIC_TYPE>bc cd</MUSIC_TYPE>");
        assertEquals(3, tokens.size());
        assertEquals("ab", tokens.get(0));
        assertEquals("<MUSIC_TYPE>bc", tokens.get(1));
        assertEquals("cd</MUSIC_TYPE>", tokens.get(2));
    }

    @Test
    public void testTrainTokenizeCJK() {
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize("放 <MUSIC>雨 一 直 下</MUSIC>");
        assertEquals(5, tokens.size());
        assertEquals("放", tokens.get(0));
        assertEquals("<MUSIC>雨", tokens.get(1));
        assertEquals("一", tokens.get(2));
        assertEquals("直", tokens.get(3));
        assertEquals("下</MUSIC>", tokens.get(4));
    }

    @Test
    public void testStandardTokenize() {
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        List<String> tokens = tokenizer.tokenize("雨韻deなタ흐르는 기억의 hello");
        System.out.println(tokens.size());
        assertEquals(8, tokens.size());
        assertEquals("雨", tokens.get(0));
        assertEquals("韻", tokens.get(1));
        assertEquals("de", tokens.get(2));
        assertEquals("な", tokens.get(3));
        assertEquals("タ", tokens.get(4));
        assertEquals("흐르는", tokens.get(5));
        assertEquals("기억의", tokens.get(6));
        assertEquals("hello", tokens.get(7));
    }

    @Test
    public void testEnStandarTokenize() {
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        List<String> tokens = tokenizer.tokenize("we're in this claire's");
        System.out.println(tokens);
    }

}
