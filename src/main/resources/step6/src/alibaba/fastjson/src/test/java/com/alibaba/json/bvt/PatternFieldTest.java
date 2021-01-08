package com.alibaba.json.bvt; // (rank 13) copied from https://github.com/alibaba/fastjson/blob/5f2c892e8df31f4539db54b1b0d467ec6821c419/src/test/java/com/alibaba/json/bvt/PatternFieldTest.java

import java.util.regex.Pattern;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class PatternFieldTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setValue(Pattern.compile("."));

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        String text = JSON.toJSONString(user, mapping, SerializerFeature.WriteMapNullValue);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getValue().pattern(), user.getValue().pattern());
    }

    public void test_codec_null() throws Exception {
        User user = new User();
        user.setValue(null);

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        String text = JSON.toJSONString(user, mapping, SerializerFeature.WriteMapNullValue);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getValue(), user.getValue());
    }

    public static class User {

        private Pattern value;

        public Pattern getValue() {
            return value;
        }

        public void setValue(Pattern value) {
            this.value = value;
        }

    }
}
