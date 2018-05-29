package xlab.parsing.dictionary;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.Util;

import java.io.IOException;

public class FSTDictionary implements Dictionary {

    private String name;
    private FST<Object> fst;

    public FSTDictionary(String name, FST<Object> fst) {
        this.fst = fst;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean contains(String str) {
        try {
            return null != Util.get(fst, new BytesRef(str));
        } catch (IOException e) {
            return false;
        }
    }
}
