package xlab.parsing.rule;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class RuleConstants {

    public final static String MUSIC_MAGIC_TOKEN = "musicmagictoken";

    public final static String ARTIST_MAGIC_TOKEN = "artistmagictoken";

    public static Map<String, String> RULE_SLOT_MAP = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
            .put("MUSIC", MUSIC_MAGIC_TOKEN)
            .put("ARTIST", ARTIST_MAGIC_TOKEN)
            .build());
}
