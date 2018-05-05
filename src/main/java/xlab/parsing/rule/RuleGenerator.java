package xlab.parsing.rule;

import xlab.parsing.exception.SchemaException;
import xlab.parsing.schema.LanguageSchema;
import xlab.parsing.schema.Slot;
import xlab.parsing.schema.SlotSet;
import xlab.parsing.schema.Template;
import xlab.parsing.tokenizer.SimpleTokenizer;
import xlab.parsing.util.SchemaUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.*;
import java.util.*;

public class RuleGenerator {

    class AutomatonWithOptional {
        Automaton automaton;
        boolean isOptional;

        public Automaton toFinalAutomaton() {
            if (isOptional) {
                return Operations.optional(automaton);
            }
            return automaton;
        }

        public AutomatonWithOptional(Automaton automaton, boolean isOptional) {
            this.automaton = automaton;
            this.isOptional = isOptional;
        }
    }

    public CharacterRunAutomaton generateRuleAutomaton(String lang) throws SchemaException {
        try {
            SchemaUtil schemaUtil = new SchemaUtil();
            LanguageSchema schema = schemaUtil.loadSchema("schema/" + lang + "_schema.json");
            SlotSet slotSet = schemaUtil.loadSlotSets("datagen_slots.json");

            List<Slot> slots = schema.getSlots();
            Map<String, Slot> slotMap = schemaUtil.loadSlotEnumValueMap(slots, slotSet);
            List<Template> templates = schema.getTemplates();
            Map<String, Template> templateMap = new HashMap<>();
            for (Template template : templates) {
                if (StringUtils.isNotBlank(template.getName())) {
                    templateMap.put(template.getName(), template);
                }
            }
            List<Automaton> automata = new ArrayList<>();
            for (Template template : schema.getTemplates()) {
                if (BooleanUtils.isTrue(template.isRule())) {
                    Automaton automaton = processTemplate(slotMap, templateMap, template.getValue());
                    automaton = MinimizationOperations.minimize(automaton, 1000000);
                    automata.add(automaton);
                }
            }
            Automaton finalAutomaton = Operations.union(automata);
            finalAutomaton = MinimizationOperations.minimize(finalAutomaton, 1000000);
            CharacterRunAutomaton runAutomaton = new CharacterRunAutomaton(finalAutomaton);
            //TODO: the automaton should be serialized in furture.
//            AutomataUtil.store(runAutomaton, new FileOutputStream(new File(outputPath)));
            return runAutomaton;
        } catch (Exception e) {
            throw new SchemaException("generate rule automaton error!", e);
        }
    }

    private Automaton processTemplate(Map<String, Slot> slotMap, Map<String, Template> templateMap, String templateValue)
            throws SchemaException {
        String[] subPatterns = templateValue.split("[|]");
        List<Automaton> automata = new ArrayList<>();
        for (String subPattern: subPatterns) {
            subPattern = subPattern.trim();
            AutomatonWithOptional automatonWithOptional = processSegment(slotMap, templateMap, subPattern.split(" "), 0);
            if (automatonWithOptional != null) {
                automata.add(automatonWithOptional.toFinalAutomaton());
            }
        }
        return Operations.union(automata);
    }

    private AutomatonWithOptional processSegment(Map<String, Slot> slotMap, Map<String, Template> templateMap, String[] segs, int i)
            throws SchemaException {
        if (i < 0 || i >= segs.length) return null;
        Automaton firstAutomaton = null;
        AutomatonWithOptional restAutomaton = processSegment(slotMap, templateMap, segs, i+1);
        RegExp space = new RegExp("[ \t]+");
        Automaton spaceAutomaton = space.toAutomaton();
        String seg = segs[i];
        boolean isOptional = false;
        if (seg.startsWith("<")) {
            String templateName = null;
            if (seg.endsWith(">")) {
                templateName = seg.substring(1, seg.length() - 1);
            } else if (seg.endsWith(">?")) {
                templateName = seg.substring(1, seg.length() - 2);
                isOptional = true;
            }
            if (!templateMap.containsKey(templateName)) {
                throw new SchemaException("can't find template name:" + templateName);
            }
            firstAutomaton = processTemplate(slotMap, templateMap, templateMap.get(templateName).getValue());
        } else {
            List<String> candidates = new ArrayList<>();
            if (seg.startsWith("{")) {
                String slotName = null;
                if (seg.endsWith("}")) {
                    slotName = seg.substring(1, seg.length() - 1);
                } else if (seg.endsWith("}?")) {
                    slotName = seg.substring(1, seg.length() - 2);
                    isOptional = true;
                }
                if (!slotMap.containsKey(slotName)) {
                    throw new SchemaException("can't find slot name:" + slotName);
                }
                // replace slot value with magic token for some slot which has large value set.
                if (RuleConstants.RULE_SLOT_MAP.containsKey(slotName)) {
                    candidates.add(RuleConstants.RULE_SLOT_MAP.get(slotName));
                } else {
                    candidates.addAll(slotMap.get(slotName).getValues());
                }
            } else if (seg.startsWith("(")) {
                String enums = null;
                if (seg.endsWith(")")) {
                    enums = seg.substring(1, seg.length() - 1);
                } else if (seg.endsWith(")?")) {
                    enums = seg.substring(1, seg.length() - 2);
                    isOptional = true;
                }
                String[] enumValues = enums.split("\\|");
                candidates = Arrays.asList(enumValues);
            } else {
                candidates = Arrays.asList(seg);
            }
            if (!candidates.isEmpty()) {
                firstAutomaton = generateAutomaton(candidates);
            }
        }
        if (firstAutomaton == null) {
            return restAutomaton;
        }
        if (restAutomaton == null) {
            return new AutomatonWithOptional(firstAutomaton, isOptional);
        }
        if (isOptional) {
            // first? rest?
            if (restAutomaton.isOptional) {
                List<Automaton> unionAutomaton = new ArrayList<>();
                unionAutomaton.add(firstAutomaton);
                unionAutomaton.add(restAutomaton.automaton);
                unionAutomaton.add(Operations.concatenate(Arrays.asList(firstAutomaton, spaceAutomaton, restAutomaton.automaton)));
                return new AutomatonWithOptional(Operations.union(unionAutomaton), true);
            } else {
                // first? rest
                List<Automaton> unionAutomaton = new ArrayList<>();
                unionAutomaton.add(restAutomaton.automaton);
                unionAutomaton.add(Operations.concatenate(Arrays.asList(firstAutomaton, spaceAutomaton, restAutomaton.automaton)));
                return new AutomatonWithOptional(Operations.union(unionAutomaton), false);
            }
        } else {
            // first rest?
            if (restAutomaton.isOptional) {
                List<Automaton> unionAutomaton = new ArrayList<>();
                unionAutomaton.add(firstAutomaton);
                unionAutomaton.add(Operations.concatenate(Arrays.asList(firstAutomaton, spaceAutomaton, restAutomaton.automaton)));
                return new AutomatonWithOptional(Operations.union(unionAutomaton), false);
            } else {
                // first rest
                return new AutomatonWithOptional(
                        Operations.concatenate(Arrays.asList(firstAutomaton, spaceAutomaton, restAutomaton.automaton)),
                        false);
            }
        }
    }

    private Automaton generateAutomaton(List<String> strs) {
        List<BytesRef> tokenizedStrs = new ArrayList<>();
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        for (String str : strs) {
            tokenizedStrs.add(new BytesRef(String.join(" ", tokenizer.tokenize(str))));
        }
        Collections.sort(tokenizedStrs);
        return Automata.makeStringUnion(tokenizedStrs);
    }

}
