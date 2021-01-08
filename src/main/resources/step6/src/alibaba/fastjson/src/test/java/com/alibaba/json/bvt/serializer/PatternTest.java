package com.alibaba.json.bvt.serializer; // (rank 13) copied from https://github.com/alibaba/fastjson/blob/5f2c892e8df31f4539db54b1b0d467ec6821c419/src/test/java/com/alibaba/json/bvt/serializer/PatternTest.java

import java.util.regex.Pattern;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class PatternTest extends TestCase {

    public void test_file() throws Exception {
        Pattern p = Pattern.compile("a*b");

        String text = JSON.toJSONString(p);

        Assert.assertEquals(JSON.toJSONString(p.pattern()), text);

        Pattern p1 = JSON.parseObject(text, Pattern.class);
        Assert.assertEquals(p.pattern(), p1.pattern());
    }
}
