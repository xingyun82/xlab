package xlab.parsing.schema;

import java.util.List;

public class Slot {

    // Following slots should be tagged.
    public final static String MUSIC = "MUSIC";
    public final static String MUSIC_TYPE = "MUSIC_TYPE";
    public final static String ARTIST = "ARTIST";
    public final static String GENRE = "GENRE";

    public final static String OTHER = "O";

    private String id;
    private String name;
    private String source;
    private List<String> values;
    private SlotType type;
    private Boolean tagging;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public SlotType getType() {
        return type;
    }

    public Boolean getTagging() {
        return tagging;
    }
}
