package xlab.parsing.datagen;

import xlab.parsing.tokenizer.SimpleTokenizer;
import xlab.parsing.util.ParsingConfig;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;


public class MusicDataGenerator {

    private String removeSuffix(String str, String chars) {
        for (int i=0; i<chars.length(); ++i) {
            int pos = str.indexOf(chars.charAt(i));
            if ( pos != -1) {
                return str.substring(0, pos);
            }
        }
        return str;
    }

    private String getSongName(Document document, Set<String> locales) {
        String songName = document.get("songName");
        String audioLocale = document.get("audioLocale");
        String artistAdamId = document.get("artistAdamId");
        String isDeleted = document.get("isDeleted");
        songName = removeSuffix(songName, "([-{（【");
        if (locales != null && !locales.contains(audioLocale)) {
            return null;
        }
        if (BooleanUtils.isTrue(BooleanUtils.toBooleanObject(isDeleted))) {
            return null;
        }
        return songName;
    }

    public void generateMusicData(String type, String indexPath, String outputPath, Set<String> locales, int count) throws Exception {
        int tempCount = 0;
        SimpleTokenizer simpleTokenizer = new SimpleTokenizer();
        try (PrintWriter pw = new PrintWriter(new File(outputPath))) {
            IndexReader r = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
            Bits bits = MultiFields.getLiveDocs(r);
            for (int i = 0; i < r.maxDoc(); ++i) {
                if (i % 10000 == 0) {
                    System.out.println(i / 10000);
                }
                if (!bits.get(i)) {
                    continue;
                }
                String name = null;
                if ("song".equals(type)) {
                    name = getSongName(r.document(i), locales);
                } else if ("playlist".equals(type)) {
                    //TODO:
                }
                if (StringUtils.isBlank(name)) continue;
                pw.println(String.join(" ", simpleTokenizer.tokenize(name)));
                if (++tempCount >= count) break;
            }
            r.close();
        }
    }

    public static void main(String[] args) throws Exception {
        MusicDataGenerator musicDataGenerator = new MusicDataGenerator();
        // generate music samples
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition0",
//                ParsingConfig.DICTIONARY_PATH + "en/en_music_samples.txt",
//                Sets.newHashSet("en", "en-US", "en_US"), 1000000);
        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition0",
        ParsingConfig.DICTIONARY_PATH + "zh/zh_music_samples.txt",
        Sets.newHashSet("zh", "cmn", "zh-CN", "zh_CN"), 1000000);
        // generate all data
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition0", ParsingConfig.DICTIONARY_PATH + "all_song_0.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition1", ParsingConfig.DICTIONARY_PATH + "all_song_1.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition2", ParsingConfig.DICTIONARY_PATH + "all_song_2.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition3", ParsingConfig.DICTIONARY_PATH + "all_song_3.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition4", ParsingConfig.DICTIONARY_PATH + "all_song_4.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition5", ParsingConfig.DICTIONARY_PATH + "all_song_5.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition6", ParsingConfig.DICTIONARY_PATH + "all_song_6.txt", null);
//        musicDataGenerator.generateMusicData("song", "/Users/yun_xing/Datasets/sfsolr/Song/partition7", ParsingConfig.DICTIONARY_PATH + "all_song_7.txt", null);

    }
}
