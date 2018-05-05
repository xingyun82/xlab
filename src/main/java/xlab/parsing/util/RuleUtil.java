package xlab.parsing.util;

import xlab.parsing.exception.SchemaException;
import xlab.parsing.rule.RuleGenerator;
import xlab.parsing.rule.RuleMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleUtil {


    private static Map<String, RuleMatcher> ruleMatcherMap = new HashMap<>();

    public static void buildRuleMatcherMap(Set<String> langSet) {
        RuleGenerator ruleGenerator = new RuleGenerator();
        for (String lang : langSet) {
            try {
                RuleMatcher ruleMatcher = new RuleMatcher(ruleGenerator.generateRuleAutomaton(lang));
                ruleMatcherMap.put(lang, ruleMatcher);
            } catch (SchemaException e) {
                //TODO: log error
                e.printStackTrace();
            }
        }
    }

    public static RuleMatcher getRuleMatcher(String lang) {
        return ruleMatcherMap.get(lang);
    }
}
