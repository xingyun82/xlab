package xlab.parsing.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TrainDataTokenizer implements Tokenizer {

    private static final String TOKEN_SEPARATOR_PATTERN_STR = "[ `~!@#$%^&*()-=+\\[{\\]}|\\:;'\",?]";

    @Override
    public List<String> tokenize(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, TOKEN_SEPARATOR_PATTERN_STR);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }
}
