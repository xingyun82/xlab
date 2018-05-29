package lucene;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class LuceneAutomataTest {

    @Test
    public void test0() {

        Automaton aut1 = Automaton.makeString("hello");
        Automaton aut2 = Automaton.makeEmptyString();
        Automaton aut3 = Automaton.union(Arrays.asList(aut1, aut2));
        RunAutomaton runAutomaton = new RunAutomaton(aut3);
        System.out.println(runAutomaton.run(""));
    }

    @Test
    public void test1() {
        Automaton aut1 = Automaton.makeString("hello");
        Automaton aut2 = Automaton.makeEmptyString();
        Automaton aut3 = Automaton.concatenate(Arrays.asList(aut1, aut2));
        RunAutomaton runAutomaton = new RunAutomaton(aut3);
        System.out.println(runAutomaton.run("hello"));
    }

    @Test
    public void test4() {
        Automaton aut1 = Automaton.makeString("hello");
        aut1 = aut1.optional();
        RunAutomaton runAutomaton = new RunAutomaton(aut1);
        System.out.println(runAutomaton.run(""));
    }

    @Test
    public void test2() {
        List<Automaton> automata = new ArrayList<>();
        Automaton firstNames = Automaton.makeStringUnion("yun", "yan", "claire", "ben");
        Automaton lastNames = Automaton.makeStringUnion("xing", "fu");
        RegExp spaces = new RegExp("[ \t]+");
        automata.add(firstNames);
        automata.add(spaces.toAutomaton());
        automata.add(lastNames);
        Automaton finalAutomaton = Automaton.concatenate(automata);
        System.out.println("#states before minimization:" + finalAutomaton.getNumberOfStates());
        finalAutomaton = Automaton.minimize(finalAutomaton);
        System.out.println("#states after minimization:" + finalAutomaton.getNumberOfStates());
        // TODO: save final automaton
        RunAutomaton runAutomaton = new RunAutomaton(finalAutomaton);
        System.out.println(runAutomaton.run("yan    fu"));
    }

    @Test
    public void test3() {
        String pattern = "A B C";
        String[] segs = pattern.split(" ");
        Automaton automaton = generateAutomaton(segs, 0);
        System.out.println(automaton.run("A"));
        System.out.println(automaton.run("B"));
        System.out.println(automaton.run("C"));
        System.out.println(automaton.run("A  B"));
        System.out.println(automaton.run("A B  C"));
        System.out.println(automaton.run("B  C"));
        System.out.println(automaton.run("A  C"));
    }


    private Automaton generateAutomaton(String[] segs, int i) {
        if (i < 0 || i >= segs.length) return null;
        Automaton first = Automaton.makeString(segs[i]);
        Automaton rest = generateAutomaton(segs, i+1);
        RegExp spaces = new RegExp("[ \t]+");
        Automaton concate = null;
        if (rest != null) {
            concate = Automaton.concatenate(Arrays.asList(first, spaces.toAutomaton(), rest));
        }
        List<Automaton> notNull = new ArrayList<>();
        if (first != null) {
            notNull.add(first);
        }
        if (rest != null) {
            notNull.add(rest);
        }
        if (concate != null) {
            notNull.add(concate);
        }
        return Automaton.union(notNull);
    }



}
