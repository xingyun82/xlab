package xlab.parsing.tokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    //TODO: we should support normalizing text, like "we're" -> we are
    @Override
    public List<String> tokenize(String str) {
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

    public static void main(String[] args) throws Exception {
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        String filePath = "/opt/itms_bigdata/search/fuse/dictionary/current/zh/zh_artist.txt";
        String outputPath = filePath + ".seg";
        try(BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputPath))))
        ) {
            String line = null;
            while ((line = br.readLine()) != null) {
                pw.println(String.join(" ", tokenizer.tokenize(line)));
            }
        }
    }
}
