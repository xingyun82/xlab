package xlab.parsing.schema;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LanguageSchemaTest {

    @Test
    public void testSchemaValidation() throws Exception {
        Set<String> langs = Sets.newHashSet("en","fr", "de", "zh");
        for (String lang : langs) {
            String jsonStr = new String(Files.readAllBytes(Paths.get(LanguageSchemaTest.class.getClassLoader().getResource("schema/" + lang + "_schema.json").getFile())));
            LanguageSchema config = new Gson().fromJson(jsonStr, LanguageSchema.class);
            assertNotNull(config);
            assertTrue(!config.getSlots().isEmpty());
            assertTrue(!config.getTemplates().isEmpty());
        }
    }
}
