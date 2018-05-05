package xlab.parsing.util;

import xlab.parsing.dictionary.Dictionary;
import xlab.parsing.dictionary.FSTDictionary;
import xlab.parsing.dictionary.MapDictionary;
import xlab.parsing.exception.SchemaException;
import xlab.parsing.schema.LanguageSchema;
import xlab.parsing.schema.Slot;
import xlab.parsing.schema.SlotSet;
import xlab.parsing.schema.SlotType;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.NoOutputs;

import java.io.*;
import java.util.*;

public class SchemaUtil {

    // slot id -> dictionary
    private static Map<String, xlab.parsing.dictionary.Dictionary> globalDictionaries = new HashMap<>();

    public void loadGlobalDictionaries() throws SchemaException {
        LanguageSchema schema = loadSchema("schema/global_schema.json");
        List<Slot> slots = schema.getSlots();
        for (Slot slot : slots) {
            String slotId = slot.getId();
            xlab.parsing.dictionary.Dictionary dictionary = loadSlotDictionary(slot);
            globalDictionaries.put(slotId, dictionary);
        }
    }

    public LanguageSchema loadSchema(String schemaFile) throws SchemaException {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(schemaFile);
            String jsonStr = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            return new Gson().fromJson(jsonStr, LanguageSchema.class);
        } catch (IOException e) {
            throw new SchemaException("Load schema error", e);
        }
    }

    public SlotSet loadSlotSets(String slotConfig) throws SchemaException {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(slotConfig);
            String jsonStr = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            return new Gson().fromJson(jsonStr, SlotSet.class);
        } catch (IOException e) {
            throw new SchemaException("Load slot config error", e);
        }
    }

    private xlab.parsing.dictionary.Dictionary loadSlotDictionary(Slot slot) throws SchemaException {
        xlab.parsing.dictionary.Dictionary dictionary = null;
        if (slot.getType() == SlotType.ENUM) {
            dictionary = new MapDictionary(slot.getName(), slot.getValues());
        } else if (slot.getType() == SlotType.FILE_ENUM) {
            dictionary = new MapDictionary(slot.getName(), loadFileEnum(slot.getSource()));
        } else if (slot.getType() == SlotType.FILE_FST) {
            dictionary = new FSTDictionary(slot.getName(), loadFileFST(slot.getSource()));
        }
        return dictionary;
    }

    /**
     * Load slot value dictionary map.
     * @param slots slot items.
     * @param slotSet slot name set.
     * @return slot value dictionary map, the key is slot name.
     * @throws SchemaException
     */
    public Map<String, xlab.parsing.dictionary.Dictionary> loadSlotValueDictionaries(List<Slot> slots, SlotSet slotSet) throws SchemaException {
        Map<String, xlab.parsing.dictionary.Dictionary> dictionaryMap = new HashMap<>();
        // add global slot dictionary
        for (Map.Entry<String, xlab.parsing.dictionary.Dictionary> entry : globalDictionaries.entrySet()) {
            if (slotSet.getSlots().contains(entry.getKey())) {
                dictionaryMap.put(entry.getValue().getName(), entry.getValue());
            }
        }
        for (Slot slot : slots) {
            String slotId = slot.getId();
            if (!slotSet.getSlots().contains(slotId)) continue;
            String slotName = slot.getName();
            Dictionary dictionary = loadSlotDictionary(slot);
            // locale slot dictionary may overwrite global one.
            dictionaryMap.put(slotName, dictionary);
        }
        return dictionaryMap;
    }

    /**
     * load slot value map only for enum type.
     * @param slots slot items.
     * @param slotSet slot name set.
     * @return
     * @throws SchemaException
     */
    public Map<String, Slot> loadSlotEnumValueMap(List<Slot> slots, SlotSet slotSet) throws SchemaException {
        Map<String, Slot> slotMap = new HashMap<>();
        SchemaUtil schemaUtil = new SchemaUtil();
        for (Slot slot: slots) {
            if (!slotSet.getSlots().contains(slot.getId())) continue;
            String slotName = slot.getName();
            if (slot.getType() == SlotType.FILE_ENUM) {
                slot.setValues(schemaUtil.loadFileEnum(slot.getSource()));
            }
            slotMap.put(slotName, slot);
        }
        return slotMap;
    }

    public FST<Object> loadFileFST(String filePath) throws SchemaException {
        try {
            NoOutputs outputs = NoOutputs.getSingleton();
            return FST.read(new File(filePath), outputs);
        } catch (IOException e) {
            throw new SchemaException("load fst error", e);
        }
    }

    public List<String> loadFileEnum(String filePath) throws SchemaException {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                result.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new SchemaException("Load slot values from file error", e);
        }
        return result;
    }

//    public Map<String, Set<String>> getInvertedSlotValueMap(List<Slot> slots) throws SchemaException {
//        Map<String, Set<String>> slotMap = new HashMap<>();
//        for (Slot slot: slots) {
//            String slotName = slot.getName();
//            List<String> values = null;
//            if (slot.getType() == SlotType.ENUM) {
//                values = slot.getValues().stream().map(str -> str.toLowerCase()).collect(Collectors.toList());
//            } else if (slot.getType() == SlotType.FILE_ENUM) {
//                values = loadFileEnum(slot.getSource());
//            }
//            for (String value : values) {
//                if (StringUtils.isBlank(value)) continue;
//                if (!slotMap.containsKey(value)) {
//                    slotMap.put(value, new HashSet<>());
//                }
//                slotMap.get(value).add(slotName);
//            }
//        }
//        return slotMap;
//    }
}
