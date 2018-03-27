package levenshtein_automata;

import com.github.liblevenshtein.transducer.Algorithm;
import com.github.liblevenshtein.transducer.Candidate;
import com.github.liblevenshtein.transducer.ITransducer;
import com.github.liblevenshtein.transducer.Transducer;
import com.github.liblevenshtein.transducer.factory.TransducerBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;

import com.github.liblevenshtein.collection.dictionary.SortedDawg;

import java.util.Collections;
import java.util.List;

public class LevenshteinAutomataTest {

    @Test
    public void test() {

        List<String> words = Lists.newArrayList("the", "be", "to", "of", "and", "that", "have");
        Collections.sort(words);
        SortedDawg dictionary = new SortedDawg(words);
        ITransducer<Candidate> transducer = new TransducerBuilder()
                .dictionary(dictionary)
                .algorithm(Algorithm.TRANSPOSITION)
                .defaultMaxDistance(2)
                .includeDistance(true)
                .build();

        for (Candidate candidate : transducer.transduce("this")) {
            System.out.println(candidate.term() + ":" + candidate.distance());

        }

    }
}
