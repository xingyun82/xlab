package xlab.parsing.util;

import xlab.parsing.schema.Slot;
import xlab.parsing.rule.RuleConstants;
import xlab.parsing.rule.RuleMatcher;
import xlab.parsing.tokenizer.SimpleTokenizer;
import com.github.jcrfsuite.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TagResultUtil {


    public List<Pair<String, String>> getActualResult(List<Pair<String, String>> tokenPairs, List<Pair<String, Double>> tagPairs) {
        List<Pair<String, String>> actualResult = new ArrayList<>();
        for (int i=0; i<tokenPairs.size(); ++i) {
            actualResult.add(new Pair<>(tokenPairs.get(i).first, tagPairs.get(i).first));
//            System.out.println(tokenPairs.get(i).first + "," + tagPairs.get(i).first + "," + tagPairs.get(i).second);
        }
        return actualResult;
    }

    private String getSlotNameFromTag(String tag) {
        if (Slot.OTHER.equals(tag)) {
            return Slot.OTHER;
        }
        int idx = tag.indexOf("-");
        return tag.substring(0, idx);
    }

    public List<Pair<String, String>> getSlotsFromTagResult(List<Pair<String, String>> tokenPairs) {
        List<Pair<String, String>> slots = new ArrayList<>();
        String currentSlotName = null;
        String currentSlotValue = "";
        for (Pair<String, String> tokenPair : tokenPairs) {
            String token = tokenPair.getFirst();
            String tag = tokenPair.getSecond();
            String slotName = getSlotNameFromTag(tag);
            if (!slotName.equals(currentSlotName)) {
                if (StringUtils.isNotBlank(currentSlotValue)) {
                    slots.add(new Pair<>(currentSlotName, currentSlotValue.trim()));
                }
                currentSlotValue = "";
            }
            currentSlotName = slotName;
            if (CharacterUtil.isChineseChar(token.charAt(0)) || CharacterUtil.isJapneseChar(token.charAt(0))) {
                currentSlotValue += token;
            } else {
                currentSlotValue += " " + token;
            }
        }
        if (StringUtils.isNotBlank(currentSlotValue)) {
            slots.add(new Pair<>(currentSlotName, currentSlotValue.trim()));
        }
        return slots;
    }


    public boolean matchRule(RuleMatcher ruleMatcher, List<Pair<String, String>> slotValuePairs) {
        StringBuilder sb = new StringBuilder();
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        for (Pair<String, String> slotValuePair : slotValuePairs) {
            String slotValue = slotValuePair.getSecond();
            String slotName = slotValuePair.getFirst();
            if (RuleConstants.RULE_SLOT_MAP.containsKey(slotName)) {
                slotValue = RuleConstants.RULE_SLOT_MAP.get(slotName);
            }
            sb.append(String.join(" ", tokenizer.tokenize(slotValue))).append(" ");
        }
        String rewriteStr = sb.toString().trim();
        return ruleMatcher.isMatch(rewriteStr);
    }

    public String printResult(List<Pair<String, String>> tokenPairs) {
        StringBuffer sb = new StringBuffer();
        String tag = null;
        for (int i=0; i<tokenPairs.size(); ++i) {
            if ("O".equals(tokenPairs.get(i).second)) {
                if (tag != null) {
                    sb.append("</" + tag + ">");
                }
                sb.append(" " + tokenPairs.get(i).first);
                tag = null;
            } else {
                int idx = tokenPairs.get(i).second.indexOf("-");
                String tmpTag = tokenPairs.get(i).second.substring(0, idx);
                if (!tmpTag.equals(tag)) {
                    if (StringUtils.isNotBlank(tag) && !tag.equals("O")) {
                        sb.append("</" + tag + ">");
                    }
                    sb.append(" <" + tmpTag + ">" + tokenPairs.get(i).first);
                } else {
                    sb.append(" " + tokenPairs.get(i).first);
                }
                tag = tmpTag;
            }
        }
        if (tag != null) {
            sb.append("</" + tag + ">");
        }
        return sb.toString().trim();
    }

}
