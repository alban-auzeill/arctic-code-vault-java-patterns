package com.alibaba.json.bvt.parser.deser; // (rank 13) copied from https://github.com/alibaba/fastjson/blob/5f2c892e8df31f4539db54b1b0d467ec6821c419/src/test/java/com/alibaba/json/bvt/parser/deser/PatternDeserializerTest.java

import java.util.regex.Pattern;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.MiscCodec;

import junit.framework.TestCase;

public class PatternDeserializerTest extends TestCase {

    public void test_pattern() throws Exception {
        Assert.assertEquals(Pattern.compile("abc").pattern(), JSON.parseObject("'abc'", Pattern.class).pattern());

        Assert.assertEquals(null, JSON.parseObject("null", Pattern.class));

        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, MiscCodec.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_STRING, MiscCodec.instance.getFastMatchToken());
    }
}
