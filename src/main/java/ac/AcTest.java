package ac;

import org.ahocorasick.trie.Trie;
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

public class AcTest {

    public static void main(String[] args) throws Exception {
        Trie.TrieBuilder trieBuilder = Trie.builder();
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
            trieBuilder = trieBuilder.addKeyword(name);
        }
        r.close();
        Trie trie = trieBuilder.build();
        System.out.println("build trie finished!");


    }
}
