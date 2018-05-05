package xlab.parsing.datagen;

import xlab.parsing.util.ParsingConfig;
import xlab.parsing.tokenizer.SimpleTokenizer;

import java.io.*;

public class ArtistDataGenerator {

    public void generateArtistList(String storeFrontId, String file) throws Exception {
        SimpleTokenizer simpleTokenizer = new SimpleTokenizer();
        try(PrintWriter pw = new PrintWriter(new File(ParsingConfig.DICTIONARY_PATH + file))) {
            BufferedReader br = new BufferedReader(new FileReader(new File("/opt/itms_cache/QueryClassifier/current/queryToAdamIdClassifier.txt")));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] segs = line.split("\t");
                if (storeFrontId.equals(segs[0])) {
                    pw.println(String.join(" ", simpleTokenizer.tokenize(segs[2])));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ArtistDataGenerator artistListGenerator = new ArtistDataGenerator();
        artistListGenerator.generateArtistList("143441", "en/en_artist.txt");
    }
}
