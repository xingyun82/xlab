package cn;

import org.junit.Test;
import org.nlpcn.commons.lang.jianfan.JianFan;
import org.nlpcn.commons.lang.pinyin.Pinyin;

import java.util.List;

public class NLPCNTest {

    @Test
    public void testFanJian() {
        System.out.println(JianFan.f2j("光碟"));
        System.out.println(JianFan.j2f("光盘"));
    }

    @Test
    public void testPinyin() {
        List<String> result = Pinyin.pinyin("雨一直下");
        for (String str : result) {
            System.out.println(str);
        }
    }
}
