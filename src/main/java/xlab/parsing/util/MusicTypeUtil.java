package xlab.parsing.util;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicTypeUtil {

    private static Map<String, Map<String, String>> musicTypeInvertedMap = new HashMap<>();

    static {
        try {
            InputStream is = MusicTypeUtil.class.getClassLoader().getResourceAsStream("music_type.json");
            String jsonStr = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>(){}.getType();
            Map<String, Map<String, List<String>>> musicTypeMap = new Gson().fromJson(jsonStr, type);
            for (Map.Entry<String, Map<String, List<String>>> entry : musicTypeMap.entrySet()) {
                Map<String, String> localeMap = new HashMap<>();
                for (Map.Entry<String, List<String>> localeMapEntry : entry.getValue().entrySet()) {
                    for (String localeString : localeMapEntry.getValue()) {
                        localeMap.put(localeString, localeMapEntry.getKey());
                    }
                }
                musicTypeInvertedMap.put(entry.getKey(), localeMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStandardName(String str, String lang) {
        if (!musicTypeInvertedMap.containsKey(lang)) return null;
        if (!musicTypeInvertedMap.get(lang).containsKey(str)) return null;
        return musicTypeInvertedMap.get(lang).get(str);
    }
}
