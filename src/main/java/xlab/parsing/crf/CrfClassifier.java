package xlab.parsing.crf;

import xlab.parsing.evaluation.EvaluationResult;
import xlab.parsing.evaluation.Evaluator;
import xlab.parsing.util.CrfClassifierUtil;
import xlab.parsing.util.SchemaFeatureExtractorUtil;
import xlab.parsing.util.TagResultUtil;
import xlab.parsing.tokenizer.SimpleTokenizer;
import com.github.jcrfsuite.CrfTagger;
import com.github.jcrfsuite.CrfTrainer;
import com.github.jcrfsuite.util.Pair;
import com.google.common.collect.Sets;
import third_party.org.chokkan.crfsuite.Attribute;
import third_party.org.chokkan.crfsuite.Item;
import third_party.org.chokkan.crfsuite.ItemSequence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrfClassifier {

    private CrfTagger tagger;

    private TagResultUtil tagUtil = new TagResultUtil();
    private Evaluator evaluator = new Evaluator();
    private TextProcessor textProcessor;

    public CrfClassifier(String lang) {
        textProcessor = new TextProcessor(lang);
    }

    public TextProcessor getTextProcessor() {
        return textProcessor;
    }

    public void train(List<String> corpusFiles, String trainFile, String modelFile) throws Exception {

        System.out.println("load corpus");
        List<Instance> instances = new ArrayList<>();
        for (String corpusFile : corpusFiles) {
            instances.addAll(textProcessor.transformCorpusToTrainData(corpusFile));
        }
        System.out.println("write training data file");
//        // write instances
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(trainFile)))) {
            instances.forEach(s -> pw.println(s.toString()));
        }
        System.out.println("start crf training");
        CrfTrainer.train(trainFile, modelFile);
    }

    public void loadModel(String modelFile) {
        tagger = new CrfTagger(modelFile);
    }

    public List<Pair<String, String>> tag(String line) throws Exception {
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        line = String.join(" ", tokenizer.tokenize(line));
        List<Pair<String, String>> tokenPairs = textProcessor.processLine(line);
        List<Pair<String, String>> noLabelTokenPair = removeLabels(tokenPairs);
        ItemSequence instance = transform(noLabelTokenPair);
        List<Pair<String, Double>> tagResult = tagger.tag(instance);
        return tagUtil.getActualResult(tokenPairs, tagResult);
    }

    public EvaluationResult eval(List<List<Pair<String, String>>> expectedResult) throws Exception {
        List<List<Pair<String, String>>> actualResult = new ArrayList<>();
        for (List<Pair<String, String>> tokenPairs : expectedResult) {
            List<Pair<String, String>> noLabelTokenPair = removeLabels(tokenPairs);
            List<Pair<String, Double>> tagPairs = tagger.tag(transform(noLabelTokenPair));
            actualResult.add(tagUtil.getActualResult(tokenPairs, tagPairs));
        }
        return evaluator.eval(expectedResult, actualResult);
    }

    private List<Pair<String, String>> removeLabels(List<Pair<String, String>> tokenPairs) {
        List<Pair<String, String>> result = new ArrayList<>();
        for (Pair<String, String> tokenPair : tokenPairs) {
                result.add(new Pair<>(tokenPair.first, null));
            }
        return result;
    }

    private ItemSequence transform(List<Pair<String, String>> tokenPairs) throws Exception {
        ItemSequence items = new ItemSequence();
        List<Instance> instances = textProcessor.transformTokenTags2Instances(tokenPairs);
        for (Instance instance : instances) {
//            System.out.println(instance);
            Item item = new Item();
            List<String> features = instance.getFeatures();
            for (String feature : features) {
                item.add(new Attribute(feature));
            }
            items.add(item);
        }
        return items;
    }



    public static void main(String[] args) throws Exception {

        Set<String> langSet = Sets.newHashSet("fr", "de", "en", "zh");
        SchemaFeatureExtractorUtil.buildSchemaFeatureExtractorMap(langSet);

        String language = "de";
        String corpusFile = "/Users/yun_xing/Datasets/" + language + "_corpus_auto.txt";
        String trainFile = "/Users/yun_xing/Datasets/"  + language + "_corpus_auto.txt.train";
        String testFile = "/Users/yun_xing/Datasets/"  + language + "_test.txt";
        String modelFile = "/opt/itms_bigdata/search/fuse/model/" + language + "_model";

        List<String> corpusFiles = new ArrayList<>();
        corpusFiles.add(corpusFile);

        System.out.println("start training");
        CrfClassifier classifier = new CrfClassifier(language);
        long start = System.currentTimeMillis();
        classifier.train(corpusFiles, trainFile, modelFile);
        long end = System.currentTimeMillis();
        System.out.println("training time:" + (end-start));

        CrfClassifierUtil.buildClassifierMap(langSet, "/opt/itms_bigdata/search/fuse/model/");
        System.out.println("load model");
        CrfClassifier classifier2 = CrfClassifierUtil.getClassifier(language);

        System.out.println("start testing");
        List<List<Pair<String, String>>> expectedResult = classifier2.getTextProcessor().loadCorpus(testFile);
        EvaluationResult evalResult = classifier2.eval(expectedResult);
        System.out.println(evalResult.toString());

//        TagResultUtil tagResultUtil = new TagResultUtil();
//        String result = tagResultUtil.printResult(classifier2.tag("放张宇的雨一直下"));
//        System.out.println(result);
    }
}
