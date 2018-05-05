package xlab.parsing.util;

import xlab.parsing.exception.SchemaException;
import xlab.parsing.feature.SchemaFeatureExtractor;
import xlab.parsing.schema.LanguageSchema;
import xlab.parsing.schema.SlotSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SchemaFeatureExtractorUtil {

    private static Map<String, SchemaFeatureExtractor> schemaFeatureExtractorMap = new HashMap<>();

    public static SchemaFeatureExtractor getSchemaFeatureExtractor(String lang) {
        return schemaFeatureExtractorMap.get(lang);
    }

    public static void buildSchemaFeatureExtractorMap(Set<String> langSet) throws SchemaException {
        SchemaUtil schemaUtil = new SchemaUtil();
        schemaUtil.loadGlobalDictionaries();
        SlotSet slotSet = schemaUtil.loadSlotSets("classification_slots.json");
        for (String lang : langSet) {
            LanguageSchema schema = schemaUtil.loadSchema("schema/" + lang + "_schema.json");
            SchemaFeatureExtractor schemaFeatureExtractor = new SchemaFeatureExtractor(schema, slotSet);
            schemaFeatureExtractorMap.put(lang, schemaFeatureExtractor);
        }
    }
}
