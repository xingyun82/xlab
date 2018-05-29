package xlab.parsing.util;

import xlab.parsing.crf.CrfClassifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CrfClassifierUtil {

    private static Map<String, CrfClassifier> classifierMap = new HashMap<>();

    public static void buildClassifierMap(Set<String> langSet, String modelDirectory) {
        for (String lang : langSet) {
            try {
                String modelFile = modelDirectory + "/" + lang + "_model";
                CrfClassifier classifier = new CrfClassifier(lang);
                classifier.loadModel(modelFile);
                classifierMap.put(lang, classifier);
            } catch (Exception e) {
                //TODO: log error
                e.printStackTrace();
            }
        }
    }

    public static CrfClassifier getClassifier(String lang) {
        return classifierMap.get(lang);
    }
}
