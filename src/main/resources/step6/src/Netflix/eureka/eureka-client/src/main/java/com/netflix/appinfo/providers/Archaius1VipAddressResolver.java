package com.netflix.appinfo.providers; // (rank 54) copied from https://github.com/Netflix/eureka/blob/821beb4b2e0204544c70017e1e883fcf4aa02c9e/eureka-client/src/main/java/com/netflix/appinfo/providers/Archaius1VipAddressResolver.java

import com.netflix.config.DynamicPropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Archaius1VipAddressResolver implements VipAddressResolver {

    private static final Logger logger = LoggerFactory.getLogger(Archaius1VipAddressResolver.class);

    private static final Pattern VIP_ATTRIBUTES_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    @Override
    public String resolveDeploymentContextBasedVipAddresses(String vipAddressMacro) {
        if (vipAddressMacro == null) {
            return null;
        }

        String result = vipAddressMacro;

        Matcher matcher = VIP_ATTRIBUTES_PATTERN.matcher(result);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = DynamicPropertyFactory.getInstance().getStringProperty(key, "").get();

            logger.debug("att:{}", matcher.group());
            logger.debug(", att key:{}", key);
            logger.debug(", att value:{}", value);
            logger.debug("");
            result = result.replaceAll("\\$\\{" + key + "\\}", value);
            matcher = VIP_ATTRIBUTES_PATTERN.matcher(result);
        }

        return result;
    }
}
