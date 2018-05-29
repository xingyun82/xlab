package lucene;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.MinimizationOperations;
import org.apache.lucene.util.automaton.Operations;
import org.apache.lucene.util.fst.*;
import org.junit.Test;
import util.SimpleStringTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.lucene.util.InfoStream.NO_OUTPUT;

public class LuceneFSTTest {

    @Test
    public void test() throws Exception {
        String inputValues[] = {"cat", "dog", "dogs"};
        long outputValues[] = {5, 7, 12};

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            BytesRef scratchBytes = new BytesRef(inputValues[i]);
            builder.add(Util.toIntsRef(scratchBytes, scratchInts), outputValues[i]);
        }
        FST<Long> fst = builder.finish();

        Long value = Util.get(fst, new BytesRef("dog"));
        System.out.println(value); // 7
    }

    @Test
    public void test2() throws Exception {
        String inputValues[] = {"cat", "dog", "dogs"};

        NoOutputs outputs = NoOutputs.getSingleton();
        Builder<Object> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            BytesRef scratchBytes = new BytesRef(inputValues[i]);
            builder.add(Util.toIntsRef(scratchBytes, scratchInts), outputs.getNoOutput());
        }
        FST<Object> fst = builder.finish();
        File file = new File("/tmp/lucene_fst");
        fst.save(file);

        FST<Object> fst2 = FST.read(file, outputs);
        Object value = Util.get(fst2, new BytesRef("dogg"));
        System.out.println(value); // 7
    }


    public void segmentFile(String inputFile, String outputFile) {

        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
             PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputFile))))
        ) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = String.join(" ", SimpleStringTokenizer.standardTokenize(line));
                pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildFST(String inputFile, String outputFile) {
        NoOutputs outputs = NoOutputs.getSingleton();
        Builder<Object> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        String preLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
            String line = null;
            List<String> lineBuffer = new ArrayList<>();
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals(preLine) || StringUtils.isBlank(line)) {
                    continue;
                }
                preLine = line;
                count++;
                if (count % 1000 == 0) {
                    System.out.println(count);
                }
                BytesRef scratchBytes = new BytesRef(line);
                builder.add(Util.toIntsRef(scratchBytes, scratchInts), outputs.getNoOutput());
            }
            FST<Object> fst = builder.finish();
            fst.save(new File(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testCJK() {
//        List<String> tokens = SimpleStringTokenizer.standardTokenize("호두나무");
//        System.out.println(tokens.size());
//    }

    @Test
    public void test3() throws Exception {
//        System.out.println(String.join("|", SimpleStringTokenizer.standardTokenize("호두나무")));
//        String inputFile = "/opt/itms_bigdata/search/fuse/dictionary/current/en/en_music_final.txt";
//        String outputFile = "/opt/itms_bigdata/search/fuse/dictionary/current/en/en_music_final_seg.txt";
//        segmentFile(inputFile, outputFile);

        String outputFile = "/opt/itms_bigdata/search/fuse/dictionary/current/all_song.fst";

        buildFST("/opt/itms_bigdata/search/fuse/dictionary/current/all_song.txt", outputFile);
//        NoOutputs outputs = NoOutputs.getSingleton();
//        FST<Object> fst2 = FST.read(new File(outputFile), outputs);
//        Object value = Util.get(fst2, new BytesRef("play shake"));
//        System.out.println(value);

    }
}
