package multisearch;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

public class SplitTest {

    @Test
    public void test() {
        Set<String> aliases = Sets.newHashSet();
        String aliasTerms = "selectioin radio";
        if (aliasTerms != null && !aliasTerms.isEmpty()){
            for(String alias : aliasTerms.split(",")){
                System.out.println(alias);
                aliases.add(alias.trim());
            }
        }
    }
}
