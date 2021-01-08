package com.fsck.k9.message.signature; // (rank 248) copied from https://github.com/k9mail/k-9/blob/908f77580622e6f8845bfee38a784a8a46b3795f/app/core/src/main/java/com/fsck/k9/message/signature/TextSignatureRemover.java


import java.util.regex.Pattern;


public class TextSignatureRemover {
    private static final Pattern DASH_SIGNATURE_PLAIN = Pattern.compile("\r\n-- \r\n.*", Pattern.DOTALL);


    public static String stripSignature(String content) {
        if (DASH_SIGNATURE_PLAIN.matcher(content).find()) {
            content = DASH_SIGNATURE_PLAIN.matcher(content).replaceFirst("\r\n");
        }
        return content;
    }
}
