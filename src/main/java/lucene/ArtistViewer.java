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
import java.util.HashSet;
import java.util.Set;

public class ArtistViewer {

    public static void main(String[] args) throws Exception {
        Set<String> artistTypeSet = new HashSet<>();
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/yun_xing/Datasets/fr_artist.txt")))) {
            IndexReader r = DirectoryReader.open(FSDirectory.open(new File("/Users/yun_xing/Datasets/sfsolr/Artist/index")));
            Bits bits = MultiFields.getLiveDocs(r);
            for (int i = 0; i < r.maxDoc(); ++i) {
                if (i % 10000 == 0) {
                    System.out.println(i / 10000);
                }
                if (!bits.get(i)) {
                    continue;
                }
                Document document = r.document(i);
                String adamId = document.get("adamId");
                String name = document.get("artistName");
                String artistType = document.get("artistTypeId");
                artistTypeSet.add(artistType);
//            String alias = document.get("indexAliasTerms");
//            String sfIds = document.get("storeFrontIds");
                if (artistType != null && artistType.equals("1")) {
//                    pw.println(adamId + "," + name);
                    pw.println(name);
                }
            }
            r.close();
        }
        for (String s : artistTypeSet) {
            System.out.println(s);
        }
    }
}
