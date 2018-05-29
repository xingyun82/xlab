package lucene;

import com.google.protobuf.WireFormat;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;


import java.io.File;

public class LuceneUTF8Test {

    @Test
    public void test() throws Exception {
        Directory indexDirectory = FSDirectory.open(new File("/tmp/index"));
        IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(Version.LUCENE_4_10_3,
                new StandardAnalyzer(Version.LUCENE_4_10_3)));
        Document document = new Document();

        document.add(new StringField("id", "1234", Field.Store.YES));
        document.add(new TextField("name", "L’actualité de l'économie avec BFM Business", Field.Store.YES));
        writer.addDocument(document);
        writer.close();

        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("/tmp/index")));
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(Version.LUCENE_4_10_3, "name", new StandardAnalyzer(Version.LUCENE_4_10_3));
        ScoreDoc[] hits = searcher.search(parser.parse("l'économie"), 10).scoreDocs;
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("name"));
        }
        reader.close();

    }
}
