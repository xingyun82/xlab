package xlab.parsing.datagen;

import xlab.parsing.exception.SchemaException;
import xlab.parsing.schema.LanguageSchema;
import xlab.parsing.schema.Slot;
import xlab.parsing.schema.SlotSet;
import xlab.parsing.schema.Template;
import xlab.parsing.tokenizer.SimpleTokenizer;
import xlab.parsing.util.SchemaUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class TrainingDataGenerator {

    public String randomValue(List<String> list) {
        Random rand = new Random();
        int r = rand.nextInt(list.size());
        return list.get(r);
    }

    public boolean randomBoolean() {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    public List<String> generateInstances(Map<String, Slot> slotMap, Map<String, Template> templateMap,
                                          Template template, int count) throws SchemaException {

        List<String> result = new ArrayList<>();
        for (int i=0; i<count; ++i) {
            if (i % 10000 == 0) {
                System.out.println(i);
            }
            result.add(processTemplate(slotMap, templateMap, template));
        }
        return result;
    }

    public String processTemplate(Map<String, Slot> slotMap, Map<String, Template> templateMap,
                                  Template template) throws SchemaException {

        StringBuilder sb = new StringBuilder();
        String pattern = template.getValue();
        String[] subPatterns = pattern.split("[|]");
        String subPattern = randomValue(Arrays.asList(subPatterns));
        String[] segs = subPattern.split(" ");
        for (String seg : segs) {
            processSegment(slotMap, templateMap, seg, sb);
        }
        return sb.toString().trim();
    }

    private void processSegment(Map<String, Slot> slotMap, Map<String, Template> templateMap,
                                String seg, StringBuilder sb) throws SchemaException {
        boolean optional = false;
        String tag = null;
        List<String> candidates = new ArrayList<>();
        if (seg.startsWith("<")) {
            String templateName = null;
            if (seg.endsWith(">")) {
                templateName = seg.substring(1, seg.length() - 1);
            } else {
                templateName = seg.substring(1, seg.length() - 2);
                optional = true;
            }
            if (!templateMap.containsKey(templateName)) {
                throw new SchemaException("can't find template name:" + templateName);
            }
            if (optional) {
                if (randomBoolean()) {
                    sb.append(processTemplate(slotMap, templateMap, templateMap.get(templateName))).append(" ");
                }
            } else {
                sb.append(processTemplate(slotMap, templateMap, templateMap.get(templateName))).append(" ");
            }
        } else {
            if (seg.startsWith("{")) {
                String slotName = null;
                if (seg.endsWith("}")) {
                    slotName = seg.substring(1, seg.length() - 1);
                } else if (seg.endsWith("}?")) {
                    slotName = seg.substring(1, seg.length() - 2);
                    optional = true;
                }
                if (!slotMap.containsKey(slotName)) {
                    throw new SchemaException("can't find slot name:" + slotName);
                }
                candidates = slotMap.get(slotName).getValues();
                if (BooleanUtils.isTrue(slotMap.get(slotName).getTagging())) {
                    tag = slotName;
                }
            } else if (seg.startsWith("(")) {
                if (seg.endsWith(")")) {
                    String enums = seg.substring(1, seg.length() - 1);
                    String[] enumValues = enums.split("\\|");
                    candidates = Arrays.asList(enumValues);
                } else if (seg.endsWith(")?")) {
                    String enums = seg.substring(1, seg.length() - 2);
                    String[] enumValues = enums.split("\\|");
                    candidates = Arrays.asList(enumValues);
                    optional = true;
                }
            } else {
                candidates = Arrays.asList(seg);
            }
            addValue(sb, candidates, optional, tag);
        }
    }


    private void addValue(StringBuilder sb, List<String> candidates, boolean optional, String tag) {
        if (candidates.size() == 0) {
            return;
        }
        String value = randomValue(candidates);
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        value = String.join(" ", tokenizer.tokenize(value));
        if (tag != null) {
            value = "<" + tag + ">" + value + "</" + tag + ">";
        }
        if (optional) {
            if (randomBoolean()) {
                sb.append(value).append(" ");
            }
        } else {
            sb.append(value).append(" ");
        }
    }


    public List<String> generateInstances(LanguageSchema config, SlotSet slotSet, int total) throws SchemaException {
        SchemaUtil schemaUtil = new SchemaUtil();
        List<String> result = new ArrayList<>();
        List<Slot> slots = config.getSlots();
        Map<String, Slot> slotMap = schemaUtil.loadSlotEnumValueMap(slots, slotSet);
        List<Template> templates = config.getTemplates();
        double sum = 0.0;
        List<Double> weights = new ArrayList<>();
        Map<String, Template> templateMap = new HashMap<>();
        for (Template template : templates) {
            if (StringUtils.isNotBlank(template.getName())) {
                templateMap.put(template.getName(), template);
            }
            Double weight = template.getWeight();
            if (weight == null) {
                weights.add(0.0);
            } else {
                weights.add(weight);
                sum += weight;
            }
        }
        for (Template template : templates) {
            Double weight = template.getWeight();
            if (weight == null || weight == 0) continue;
            int count = (int)(total*(weight/sum));
            System.out.println("generating " + count + " instances for " + template.getDescription());
            result.addAll(generateInstances(slotMap, templateMap, template, count));
        }
        return result;
    }

    public void generateInstances(String lang, String outputPath, int count) throws Exception {
        SchemaUtil schemaUtil = new SchemaUtil();
        LanguageSchema schema = schemaUtil.loadSchema("schema/" + lang + "_schema.json");
        SlotSet slotSet = schemaUtil.loadSlotSets("datagen_slots.json");

        TrainingDataGenerator generator = new TrainingDataGenerator();
        List<String> instances = generator.generateInstances(schema, slotSet, count);
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputPath)))) {
            for (String instance : instances) {
                pw.println(instance);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TrainingDataGenerator dataGenerator = new TrainingDataGenerator();
//        dataGenerator.generateInstances("en", "/Users/yun_xing/Datasets/en_corpus_auto_2.txt", 100000);
        String lang = "en";
        dataGenerator.generateInstances(lang, "/Users/yun_xing/Datasets/" + lang + "_corpus_auto.txt", 100000);
    }
}
