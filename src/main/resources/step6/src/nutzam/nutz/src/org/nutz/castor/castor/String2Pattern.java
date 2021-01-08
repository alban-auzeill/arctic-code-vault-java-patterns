package org.nutz.castor.castor; // (rank 197) copied from https://github.com/nutzam/nutz/blob/cbd8da2d74be092864b166413a2fe988bc162663/src/org/nutz/castor/castor/String2Pattern.java

import java.util.regex.Pattern;

import org.nutz.castor.Castor;
import org.nutz.castor.FailToCastObjectException;

public class String2Pattern extends Castor<String, Pattern> {

    @Override
    public Pattern cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        try {
            return Pattern.compile(src);
        }
        catch (Exception e) {
            throw new FailToCastObjectException("Error regex: " + src, e);
        }
    }

}
