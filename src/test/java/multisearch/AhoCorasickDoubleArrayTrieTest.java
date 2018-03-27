package multisearch;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import org.junit.Test;

import java.util.List;
import java.util.TreeMap;

public class AhoCorasickDoubleArrayTrieTest {

    @Test
    public void test() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("elton john", "ARTIST");
        map.put("rocket hour", "SHOW");
        map.put("kesha", "ARTIST");
        map.put("kesha doll", "ARTIST");
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<>();
        acdat.build(map);

        long start = System.currentTimeMillis();
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = acdat.parseText("elton john's rocket hour with kesha doll");
        long end = System.currentTimeMillis();
        System.out.println("matching time:" + (end-start));

        for (AhoCorasickDoubleArrayTrie<String>.Hit<String> word : wordList) {
            System.out.println(word);
        }
    }

}
