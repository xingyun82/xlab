package xlab.parsing.evaluation;

import com.github.jcrfsuite.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResult {

    private int correct;

    private int wrong;

    private List<Pair<String, String>> diffs = new ArrayList<>();

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public void addDiff(String expected, String actual) {
        diffs.add(new Pair<>(expected, actual));
    }

    public List<Pair<String, String>> getDiffs() {
        return diffs;
    }

    public double getAccuracy() {
        return correct*1.0/(correct + wrong);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("accuracy:").append(getAccuracy()).append("\n");
        sb.append("diffs:").append("\n");
        for (Pair<String, String> diff : diffs) {
            sb.append("expected: ").append(diff.getFirst()).append("\n");
            sb.append("  actual: ").append(diff.getSecond()).append("\n");
            sb.append("\n");
        }
        sb.append("#diffs:").append(diffs.size());
        return sb.toString();
    }
}
