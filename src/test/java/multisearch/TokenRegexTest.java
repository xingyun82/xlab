package multisearch;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenRegexTest {

    private static AnnotationPipeline pipeline = null;

    @Before
    public void setUp() throws Exception {
        long start = System.currentTimeMillis();
        if (pipeline == null) {
            pipeline = new AnnotationPipeline();
            pipeline.addAnnotator(new TokenizerAnnotator(false, "en"));
            //pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
            //pipeline.addAnnotator(new POSTaggerAnnotator(false));
            //pipeline.addAnnotator(new NumberAnnotator(false, false));
//        pipeline.addAnnotator(new QuantifiableEntityNormalizingAnnotator(false));
        }
        long end = System.currentTimeMillis();
        System.out.println("init time:" + (end-start));
    }

    private static CoreMap createDocument(String text) {
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        return annotation;
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        CoreMap doc = createDocument("play MUSIC_ARTIST's rocket hour with MUSIC_ARTIST");
        long end = System.currentTimeMillis();
        System.out.println("tokenize time:" + (end-start));

        long start1 = System.currentTimeMillis();
        TokenSequencePattern p = TokenSequencePattern.compile("play MUSIC_ARTIST /'s/ rocket hour with MUSIC_ARTIST");
        TokenSequenceMatcher m = p.getMatcher(doc.get(CoreAnnotations.TokensAnnotation.class));
        long end1 = System.currentTimeMillis();
        System.out.println("matching time:" + (end1-start1));

        if(m.find()) {
            System.out.println(m.group(1));
        }
    }

    @Test
    public void testPureRegex() {
        long start = System.currentTimeMillis();
        String text = "play MUSIC_ARTIST's rocket hour with MUSIC_ARTIST";

            Pattern p = Pattern.compile("play (some |latest )?MUSIC_ARTIST('s)? (.*) with MUSIC_ARTIST");
            Matcher m = p.matcher(text);
            boolean found = m.find();
            System.out.println(found);
            if (found) {
                for (int i = 0; i < m.groupCount(); ++i) {
                    System.out.println(m.group(i + 1));
                }
            }

        long end = System.currentTimeMillis();
        System.out.println("matching time:" + (end-start));
    }
}
