package csv;

import com.google.common.collect.Sets;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.collections.CollectionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CSVReaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("beats1ShowNames.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(is, Charset.forName("UTF8"));
        try (CSVReader reader = new CSVReader(inputStreamReader, '\t', CSVWriter.NO_QUOTE_CHARACTER)) {
            String[] items = null;
            while ((items = reader.readNext()) != null) {
                System.out.println(items[0]);
                System.out.println(items[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
