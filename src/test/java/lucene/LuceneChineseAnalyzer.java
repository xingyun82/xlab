package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LuceneChineseAnalyzer {

    @Test
    public void test() {
        Analyzer analyzer = new SmartChineseAnalyzer(true);
        String str = "中文維基百科將自己定位為一個包含人類所有知識領域的百科全書，而不是一本字典、詞典、論壇或任何其他性質的網站";
        List<String> result = new ArrayList<String>();
        try {
            TokenStream tokenStream = analyzer.tokenStream("field", str);
            CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.add(term.toString());
            }
            tokenStream.end();
            tokenStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        analyzer.close();
    }

    @Test
    public void test2() throws Exception {
        Reader reader = new StringReader("中文維基百科將自己定位為一個包含人類所有知識領域的百科全書，而不是一本字典、詞典、論壇或任何其他性質的網站");

        Analyzer a = new CJKAnalyzer();
        TokenStream ts = a.tokenStream("", reader);
        OffsetAttribute offsetAttribute = ts.getAttribute(OffsetAttribute.class);
        CharTermAttribute termAttribute = ts.getAttribute(CharTermAttribute.class);
        int n = 0;
        ts.reset();
        while (ts.incrementToken()) {
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = termAttribute.toString();
            n++;
            System.out.println("Token(" + n + ") 的內容為：" + term);
        }
        System.out.println("==共有詞條" + n + "條==");

        ts.end();
        ts.close();
    }

}
