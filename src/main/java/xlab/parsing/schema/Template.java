package xlab.parsing.schema;

import java.util.List;

public class Template {

    private String name;
    private String value;
    private Double weight;
    private String description;
    private Boolean isRule;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isRule() {
        return isRule;
    }
}
