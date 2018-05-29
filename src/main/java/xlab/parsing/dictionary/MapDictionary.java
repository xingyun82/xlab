package xlab.parsing.dictionary;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MapDictionary implements Dictionary {

    private String name;
    private Set<String> values;

    public MapDictionary(String name, Collection<String> values) {
        this.name = name;
        this.values = new HashSet<>(values);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean contains(String str) {
        return values.contains(str);
    }
}
