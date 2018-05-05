package xlab.parsing.crf;

import xlab.parsing.feature.SchemaFeatureExtractor;
import xlab.parsing.feature.TokenFeatureExtractor;
import xlab.parsing.tokenizer.TrainDataTokenizer;
import xlab.parsing.util.SchemaFeatureExtractorUtil;
import com.github.jcrfsuite.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor {

    private static final String TAG_SUFFIX_ENTITY_BEGIN = "-B";
    private static final String TAG_SUFFIX_ENTITY_INTERNAL = "-I";
    private static final String TAG_NON_ENTITY = "O";

    private static final Pattern preTagPattern = Pattern.compile("(<(?<tag>[^>]+)>)*(?<word>[^\\s-]*)");
    private static final Pattern sufTagPattern = Pattern.compile("(?<word>[^\\s-]*)(</(?<tag>[^>]+)>)");

    private String lang;
    private TokenFeatureExtractor tokenFeatureExtractor = new TokenFeatureExtractor();
    private SchemaFeatureExtractor phraseFeatureExtractor = null;

    public TextProcessor(String lang) {
        this.lang = lang;
        phraseFeatureExtractor = SchemaFeatureExtractorUtil.getSchemaFeatureExtractor(lang);
    }

    public List<Instance> transformCorpusToTrainData(String corpusFile) throws Exception {
        List<Instance> instances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(corpusFile))) {
            String line = br.readLine();
            while (line != null) {
                if (line.trim().startsWith("#")) {
                    line = br.readLine();
                    continue;
                }
                instances.addAll(transform(line));
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instances;
    }

    private List<Instance> transform(String line) {
        List<Pair<String, String>> tokenTags = processLine(line);
        return transformTokenTags2Instances(tokenTags);
    }

    public List<Instance> transformTokenTags2Instances(List<Pair<String, String>> tokenTags) {
        List<Instance> instances = new ArrayList<>();
        for (int i=0; i<tokenTags.size(); ++i) {
            Instance instance = new Instance();
            List<String> features = new ArrayList<>();
            features.addAll(tokenFeatureExtractor.extractFeatures(tokenTags, i, 5));
            features.addAll(phraseFeatureExtractor.extractSchemaFeatures(tokenTags, i, 5));
            instance.setFeatures(features);
            instance.setLabel(tokenTags.get(i).second);
            instances.add(instance);
        }
        return instances;
    }

    public List<Pair<String, String>> processLine(String line) {
        List<Pair<String, String>> result = new ArrayList<>();
        if (StringUtils.isBlank(line)) return result;
        String tmpTag = null;
        TrainDataTokenizer tokenizer = new TrainDataTokenizer();
        List<String> tokens = tokenizer.tokenize(line);
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

    private void reconcile(List<Pair<String, String>> tokenTagPairs) {
        String tag = null;
        for (int i=0; i<tokenTagPairs.size(); ++i) {
            Pair<String, String> tokenPair = tokenTagPairs.get(i);
            String tmpTag = tokenPair.second;
            if (tmpTag != null) {
                if (tag == null) {
                    tokenPair.second = tokenPair.second + TAG_SUFFIX_ENTITY_BEGIN;
                } else {
                    if (tag.equals(tmpTag)) {
                        tokenPair.second = tmpTag + TAG_SUFFIX_ENTITY_INTERNAL;
                    } else {
                        tokenPair.second = tmpTag + TAG_SUFFIX_ENTITY_BEGIN;
                    }
                }
            } else {
                tokenPair.second = TAG_NON_ENTITY;
            }
            tag = tmpTag;
        }
    }

    public List<List<Pair<String, String>>> loadCorpus(String corpusFile) throws Exception {
        List<List<Pair<String, String>>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(corpusFile))) {
            String line = br.readLine();
            while (line != null) {
                if (line.trim().startsWith("#")) {
                    line = br.readLine();
                    continue;
                }
                data.add(processLine(line));
                line = br.readLine();
            }
        }
        return data;
    }
}
