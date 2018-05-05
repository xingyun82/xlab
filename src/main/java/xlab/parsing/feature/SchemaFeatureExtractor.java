package xlab.parsing.feature;

import xlab.parsing.dictionary.Dictionary;
import xlab.parsing.exception.SchemaException;
import xlab.parsing.schema.LanguageSchema;
import xlab.parsing.schema.SlotSet;
import xlab.parsing.util.SchemaUtil;
import com.github.jcrfsuite.util.Pair;
import com.google.common.collect.Lists;

import java.util.*;


public class SchemaFeatureExtractor {

    private Map<String, xlab.parsing.dictionary.Dictionary> languageDict;

    public SchemaFeatureExtractor(LanguageSchema schema, SlotSet slotSet) throws SchemaException {
        SchemaUtil schemaUtil = new SchemaUtil();
        System.out.println("loading language dictionary");
        languageDict = schemaUtil.loadSlotValueDictionaries(schema.getSlots(), slotSet);
    }

    public Set<String> getSlots(String str) {
        Set<String> slotNames = new HashSet<>();
        for (Map.Entry<String, Dictionary> entry : languageDict.entrySet()) {
            if (entry.getValue().contains(str)) {
                slotNames.add(entry.getKey());
            }
        }
        return slotNames;
    }

    public List<String> extractSchemaFeatures(List<Pair<String, String>> tokenPairs, int i, int ws) {
        Set<String> features = new HashSet<>();
        Set<String> analyzedPharse = new HashSet<>();
        for (int j=i-ws; j<=i+ws; ++j) {
            for (int k=1; k<=ws+1; ++k) {
                int start = Math.max(j, 0);
                int end = Math.min(j+k, tokenPairs.size());
                if (start >= end) continue;
                List<String> tokens = new ArrayList<>();
                tokenPairs.subList(start, end).stream().forEach(t -> tokens.add(t.first));
                String phrase = String.join(" ", tokens).toLowerCase();
                if (analyzedPharse.contains(phrase)) continue;
                analyzedPharse.add(phrase);
                Set<String> slots = getSlots(phrase);
                for (String slot : slots) {
                    features.add("is" + slot + "_" + (start - i) + "_" + (end - i) + "=true");
                    if (start == i) {
                        features.add("is" + slot + "-B=true");
                    } else if (start < i && end > i) {
                        features.add("is" + slot + "-I=true");
                    }
                }
            }
        }
        return Lists.newArrayList(features);
    }
}
