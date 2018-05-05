package xlab.parsing.rule;

import org.apache.lucene.util.automaton.CharacterRunAutomaton;

public class RuleMatcher {

    private CharacterRunAutomaton runAutomaton;

    public RuleMatcher(CharacterRunAutomaton runAutomaton) {
        this.runAutomaton = runAutomaton;
    }

    public boolean isMatch(String str) {
        return runAutomaton.run(str);
    }
}
