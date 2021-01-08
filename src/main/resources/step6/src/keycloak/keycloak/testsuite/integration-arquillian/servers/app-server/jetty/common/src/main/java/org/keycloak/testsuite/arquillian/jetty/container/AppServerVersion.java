package org.keycloak.testsuite.arquillian.jetty.container; // (rank 73) copied from https://github.com/keycloak/keycloak/blob/66dfa32cd569a7416de21b4dc04db212e8fccce5/testsuite/integration-arquillian/servers/app-server/jetty/common/src/main/java/org/keycloak/testsuite/arquillian/jetty/container/AppServerVersion.java

import org.eclipse.jetty.util.Jetty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum AppServerVersion {
    INSTANCE;

    private String appServerVersion;

    AppServerVersion() {
        Pattern versionExtraction = Pattern.compile("(\\d\\.\\d).*");
        Matcher m = versionExtraction.matcher(Jetty.VERSION);
        if (!m.find()) {
            throw new IllegalStateException("Could not parse Jetty version: " + Jetty.VERSION);
        }
        appServerVersion = m.group(1).replaceAll("\\.", "");
    }

    public String getAppServerVersion() {
        return appServerVersion;
    }
}
