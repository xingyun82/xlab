package xlab.parsing.evaluation;

import xlab.parsing.util.TagResultUtil;
import com.github.jcrfsuite.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator {

    private TagResultUtil tagResultUtil = new TagResultUtil();

    public EvaluationResult eval(List<List<Pair<String, String>>> expectedResult, List<List<Pair<String, String>>> actualResult) {
        EvaluationResult evalResult = new EvaluationResult();
        Set<Integer> diffIndexSet = new HashSet<>();
        int correct = 0, wrong = 0;
        for (int i=0; i<expectedResult.size(); ++i) {
            for (int j=0; j<expectedResult.get(i).size(); ++j) {
                if (expectedResult.get(i).get(j).second.equals(actualResult.get(i).get(j).second)) {
                    correct++;
                } else {
                    wrong++;
                    if (!diffIndexSet.contains(i)) {
                        diffIndexSet.add(i);
                    }
                }
            }
            if (diffIndexSet.contains(i)) {
                String expected = tagResultUtil.printResult(expectedResult.get(i));
                String actual = tagResultUtil.printResult(actualResult.get(i));
                evalResult.addDiff(expected, actual);
            }
        }
        evalResult.setCorrect(correct);
        evalResult.setWrong(wrong);
        return evalResult;
    }
}
