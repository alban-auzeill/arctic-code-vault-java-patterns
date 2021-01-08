package com.networknt.config.yml; // (rank 285) copied from https://github.com/networknt/light-4j/blob/ea33be22f018a59cc5b902a65004fd54a2e4fce6/config/src/main/java/com/networknt/config/yml/YmlConstants.java

import java.util.regex.Pattern;

import org.yaml.snakeyaml.nodes.Tag;

public class YmlConstants {
	public static final Tag CRYPT_TAG = new Tag(Tag.PREFIX + "crypt");
	public static final Pattern CRYPT_PATTERN = Pattern.compile("^CRYPT:([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
	public static final String CRYPT_FIRST = "C"; 
}
