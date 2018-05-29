package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.StringReader;

public class LuceneAnalyzerTest {

    @Test
    public void testStandardAnalyzer() throws Exception {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        StopFilter ts = (StopFilter)analyzer.tokenStream(null, new StringReader("L’actualité de l'économie avec BFM Business"));
        CharTermAttribute ct = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            System.out.println(ct.toString());
        }
    }
}
