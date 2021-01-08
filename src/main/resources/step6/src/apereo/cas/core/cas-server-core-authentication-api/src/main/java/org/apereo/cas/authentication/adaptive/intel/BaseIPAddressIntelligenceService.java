package org.apereo.cas.authentication.adaptive.intel; // (rank 42) copied from https://github.com/apereo/cas/blob/b74039418dc6a75a2a62142f49d42ba946f03526/core/cas-server-core-authentication-api/src/main/java/org/apereo/cas/authentication/adaptive/intel/BaseIPAddressIntelligenceService.java

import org.apereo.cas.configuration.model.core.authentication.AdaptiveAuthenticationProperties;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.webflow.execution.RequestContext;

import java.util.regex.Pattern;

/**
 * This is {@link BaseIPAddressIntelligenceService}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseIPAddressIntelligenceService implements IPAddressIntelligenceService {
    /**
     * Adaptive authentication settings.
     */
    protected final AdaptiveAuthenticationProperties adaptiveAuthenticationProperties;

    private static void trackResponseInRequestContext(final RequestContext context, final IPAddressIntelligenceResponse response) {
        context.getFlowScope().put("ipAddressIntelligenceResponse", response);
    }

    private boolean isClientIpAddressRejected(final String clientIp) {
        return StringUtils.isNotBlank(this.adaptiveAuthenticationProperties.getRejectIpAddresses())
            && Pattern.compile(this.adaptiveAuthenticationProperties.getRejectIpAddresses()).matcher(clientIp).find();
    }

    @Override
    public IPAddressIntelligenceResponse examine(final RequestContext context, final String clientIpAddress) {
        if (isClientIpAddressRejected(clientIpAddress)) {
            val response = IPAddressIntelligenceResponse.banned();
            trackResponseInRequestContext(context, response);
            return response;
        }
        val response = examineInternal(context, clientIpAddress);
        trackResponseInRequestContext(context, response);
        return response;
    }

    /**
     * Examine internally and build intelligence response.
     *
     * @param context         the context
     * @param clientIpAddress the client ip address
     * @return the ip address intelligence response
     */
    public abstract IPAddressIntelligenceResponse examineInternal(RequestContext context, String clientIpAddress);
}
