package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SongViewer {

    public static void main(String[] args) throws Exception {
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/yun_xing/Datasets/fr_music.txt")))) {
            IndexReader r = DirectoryReader.open(FSDirectory.open(new File("/Users/yun_xing/Datasets/sfsolr/Song/partition0")));
            Bits bits = MultiFields.getLiveDocs(r);
            for (int i = 0; i < r.maxDoc(); ++i) {
//            for (int i = 0; i < 1000; ++i) {
                if (i % 10000 == 0) {
                    System.out.println(i / 10000);
                }
                if (!bits.get(i)) {
                    continue;
                }
                Document document = r.document(i);
                String adamId = document.get("adamId");
                String name = document.get("songName");
                String locale = document.get("audioLocale");
                String artistAdamId = document.get("artistAdamId");
                int pos = name.indexOf("(");
                if (pos != -1) {
                    name = name.substring(0, pos).trim();
                }
                if ("fr".equals(locale) && !artistAdamId.equals("386217870")) {
//                    pw.println(adamId + "\t" + locale + "\t" + name);
                    pw.println(name);
                }
            }
            r.close();
        }
    }
}
