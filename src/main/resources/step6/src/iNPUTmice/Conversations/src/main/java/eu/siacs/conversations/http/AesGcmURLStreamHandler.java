package eu.siacs.conversations.http; // (rank 693) copied from https://github.com/iNPUTmice/Conversations/blob/364502d1a3def0e8895c463b72c8f48e45508214/src/main/java/eu/siacs/conversations/http/AesGcmURLStreamHandler.java

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.regex.Pattern;


public class AesGcmURLStreamHandler extends URLStreamHandler {

    /**
     * This matches a 48 or 44 byte IV + KEY hex combo, like used in http/aesgcm upload anchors
     */
    public static final Pattern IV_KEY = Pattern.compile("([A-Fa-f0-9]{2}){48}|([A-Fa-f0-9]{2}){44}");

    public static final String PROTOCOL_NAME = "aesgcm";

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new URL("https"+url.toString().substring(url.getProtocol().length())).openConnection();
    }
}
