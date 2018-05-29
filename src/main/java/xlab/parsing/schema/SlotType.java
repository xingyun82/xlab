package xlab.parsing.schema;

import com.google.gson.annotations.SerializedName;

public enum SlotType {

    @SerializedName("enum")
    ENUM("enum"),

    @SerializedName("file_enum")
    FILE_ENUM("file_enum"),

    @SerializedName("file_fst")
    FILE_FST("file_fst");

    private String name;

    SlotType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
