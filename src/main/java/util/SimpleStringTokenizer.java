package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SimpleStringTokenizer {

    private static final String TOKEN_SEPARATOR_PATTERN_STR = "[ `~!@#$%^&*()-=+\\[{\\]}|\\:;'\",?]";

    public static List<String> trainDataTokenize(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, TOKEN_SEPARATOR_PATTERN_STR);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken().toLowerCase());
        }
        return tokens;
    }

    public static List<String> standardTokenize(String str) {
        List<String> result = new ArrayList<>();
        try {
            StandardTokenizer tokenizer = new StandardTokenizer(
                    AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, new StringReader(str));
            tokenizer.reset();
            CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);
            while (tokenizer.incrementToken()) {
                String term = attr.toString();
                if (StringUtils.isNotBlank(term)) {
                    result.add(term.toLowerCase());
                }
            }
        } catch (IOException e) {
            //TODO:
            e.printStackTrace();
        }
        return result;
    }

}
