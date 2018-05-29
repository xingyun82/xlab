package xlab.parsing.feature;

import com.github.jcrfsuite.util.Pair;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class TokenFeatureExtractor {

    public List<String> extractFeatures(List<Pair<String, String>> tokenPairs, int i, int ws) {
        List<String> features = new ArrayList<>();
        for (int k=-ws; k<=ws; ++k) {
            features.addAll(extractTokenFeatures(tokenPairs, i, k));
        }
        return features;
    }

    private List<String> extractTokenFeatures(List<Pair<String, String>> tokenPairs, int i, int index) {
        int j = i + index;
        if (j < 0 || j >= tokenPairs.size()) return new ArrayList<>();
        String token = tokenPairs.get(j).first;
        List<String> features = new ArrayList<>();
        features.add("w_" + index + "=" + token);
//        features.add("w_" + index + "_len=" + token.length());
        features.add("w_" + index + "_isNum=" + StringUtils.isNumeric(token));
        return features;
    }

}
