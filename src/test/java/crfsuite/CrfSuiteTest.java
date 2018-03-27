package crfsuite;

import com.github.jcrfsuite.CrfTagger;
import com.github.jcrfsuite.CrfTrainer;

import com.github.jcrfsuite.util.Pair;
import org.apache.commons.lang3.StringUtils;
import third_party.org.chokkan.crfsuite.Attribute;
import third_party.org.chokkan.crfsuite.Item;
import third_party.org.chokkan.crfsuite.ItemSequence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrfSuiteTest {

    Pattern preTagPattern = Pattern.compile("(<(?<tag>[^>]+)>)*(?<word>[^\\s-]*)");
    Pattern sufTagPattern = Pattern.compile("(?<word>[^\\s-]*)(</(?<tag>[^>]+)>)");

    public static String corpusFile = "/Users/yun_xing/Datasets/fr_corpus.txt";
    public static String trainFile = "/Users/yun_xing/Datasets/fr_train.txt";
    public static String modelFile = "/Users/yun_xing/Datasets/fr_model";
    public static String testFile = "/Users/yun_xing/Datasets/fr_corpus.txt";

    public CrfTagger loadModel(String modelFile) {
        return new CrfTagger(modelFile);
    }

    private void train(String trainFile, String modelFile) throws Exception {
        transform(corpusFile, trainFile);
        CrfTrainer.train(trainFile, modelFile);
    }

    private void test(CrfTagger tagger, String testFile) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line = br.readLine();
            while (line != null) {
                if (line.trim().startsWith("#")) {
                    line = br.readLine();
                    continue;
                }
                tagLine(tagger, line);
                line = br.readLine();
            }
        }
    }

    private void tagLine(CrfTagger tagger, String line) throws Exception {
        List<Pair<String, String>> tokenPairs = processLine(line);
        List<Pair<String, Double>> result = tagger.tag(transform(tokenPairs));
        for (int i=0; i<tokenPairs.size(); ++i) {
            tokenPairs.get(i).second = result.get(i).first;
        }
        System.out.println(printResult(tokenPairs));
    }

    public static void main(String[] args) throws Exception {
        CrfSuiteTest tester = new CrfSuiteTest();
        tester.train(trainFile, modelFile);
        CrfTagger tagger = tester.loadModel(modelFile);
        tester.test(tagger, testFile);
//        tester.tagLine(tagger, "écouter 13 block vide");
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


    private ItemSequence transform(List<Pair<String, String>> tokenPairs) throws Exception {
        ItemSequence items = new ItemSequence();
        List<Instance> instances = transformTokenTags2Instances(tokenPairs);
        for (Instance instance : instances) {
            Item item = new Item();
            List<String> features = instance.getFeatures();
            for (String feature : features) {
                item.add(new Attribute(feature));
            }
            items.add(item);
        }
        return items;
    }

    private void transform(String corpusFile, String trainFile) throws Exception {

        List<Instance> instances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(corpusFile))) {
            String line = br.readLine();
            while (line != null) {
                if (line.trim().startsWith("#")) {
                    line = br.readLine();
                    continue;
                }
                List<Pair<String, String>> tokenTags = processLine(line);
                instances.addAll(transformTokenTags2Instances(tokenTags));
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // write instances
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(trainFile)))) {
            instances.forEach(s -> pw.println(s.toString()));
        }
    }


    private List<Instance> transformTokenTags2Instances(List<Pair<String, String>> tokenTags) {
        List<Instance> instances = new ArrayList<>();
        for (int i=0; i<tokenTags.size(); ++i) {
            Instance instance = new Instance();
            instance.setFeatures(extractFeatures(tokenTags, i));
            instance.setLabel(tokenTags.get(i).second);
            instances.add(instance);
        }
        return instances;
    }

    // extract features from context
    // w-1, w0, w1
    // l-1, l0, l1
    // w: text, length, isNumber, hasInternalPunc

    private List<String> extractFeatures(List<Pair<String, String>> tokenPairs, int i) {
        List<String> features = new ArrayList<>();
        features.addAll(extractTokenFeatures(tokenPairs, i, -2));
        features.addAll(extractTokenFeatures(tokenPairs, i, -1));
        features.addAll(extractTokenFeatures(tokenPairs, i, 0));
        features.addAll(extractTokenFeatures(tokenPairs, i, 1));
        features.addAll(extractTokenFeatures(tokenPairs, i, 2));
//        features.addAll(extractLabelFeatures(tokenPairs, i, -1));
//        features.addAll(extractLabelFeatures(tokenPairs, i, 0));
//        features.addAll(extractLabelFeatures(tokenPairs, i, 1));
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

    private List<String> extractLabelFeatures(List<Pair<String, String>> tokenPairs, int i, int index) {
        int j = i + index;
        if (j < 0 || j >= tokenPairs.size()) return new ArrayList<>();
        String label = tokenPairs.get(j).second;
        List<String> features = new ArrayList<>();
        features.add("l_" + index + "=" + label);
        return features;
    }

    private void reconcile(List<Pair<String, String>> tokenTagPairs) {
        String tag = null;
        for (int i=0; i<tokenTagPairs.size(); ++i) {
            Pair<String, String> tokenPair = tokenTagPairs.get(i);
            String tmpTag = tokenPair.second;
            if (tmpTag != null) {
                if (tag == null) {
                    tokenPair.second = tokenPair.second + "-B";
                } else {
                    if (tag.equals(tmpTag)) {
                        tokenPair.second = tmpTag + "-I";
                    } else {
                        tokenPair.second = tmpTag + "-B";
                    }
                }
            } else {
                tokenPair.second = "O";
            }
            tag = tmpTag;
        }
    }

    private List<Pair<String, String>> processLine(String line) {
        List<Pair<String, String>> result = new ArrayList<>();
        if (StringUtils.isBlank(line)) return result;
        String tmpTag = null;

        String spacePattern = "( |'|-)";
        String[] tokens = line.split(spacePattern);
        for (String token : tokens) {
            Matcher m = preTagPattern.matcher(token);
            if (m.find()) {
                String tag = m.group("tag");
                String word = m.group("word");
                if (tag != null) {
                    tmpTag = tag;
                }
                if (word != null) {
                    Matcher wordM = sufTagPattern.matcher(word);
                    if (wordM.find()) {
                        String sufTag = wordM.group("tag");
                        String sufWord = wordM.group("word");
                        if (sufWord != null) {
                            result.add(new Pair<>(sufWord.toLowerCase(), tmpTag));
                        }
                        if (sufTag != null) {
                            tmpTag = null;
                        }
                    } else {
                        if (StringUtils.isNotBlank(word)) {
                            result.add(new Pair<>(word.toLowerCase(), tmpTag));
                        }
                    }
                }
            } else {
                if (StringUtils.isNotBlank(token)) {
                    result.add(new Pair<>(token.toLowerCase(), tmpTag));
                }
            }
        }
        reconcile(result);
        return result;
    }


}
