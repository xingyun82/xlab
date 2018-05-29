package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;

import java.io.*;

public class QueryClassifierViewer {

    public static void main(String[] args) throws Exception {
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/yun_xing/Datasets/fr_artist.txt")))) {
            BufferedReader br = new BufferedReader(new FileReader(new File("/opt/itms_cache/QueryClassifier/current/queryToAdamIdClassifier.txt")));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] segs = line.split("\t");
                if ("143442".equals(segs[0])) {
                    pw.println(segs[2]);
                }
            }
        }
    }
}
