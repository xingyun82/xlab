package xlab.parsing.crf;

import java.util.List;

public class Instance {

    private List<String> features;

    private String label;

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label + "\t" +  String.join("\t", features);
    }
}
