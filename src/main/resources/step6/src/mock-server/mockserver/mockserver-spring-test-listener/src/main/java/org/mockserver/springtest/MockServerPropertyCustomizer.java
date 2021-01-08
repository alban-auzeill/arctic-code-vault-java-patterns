package org.mockserver.springtest; // (rank 312) copied from https://github.com/mock-server/mockserver/blob/7f962c8fc5fa0add558b10315aa4ae50be1e7934/mockserver-spring-test-listener/src/main/java/org/mockserver/springtest/MockServerPropertyCustomizer.java

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MockServerPropertyCustomizer implements ContextCustomizer {
    private static final int mockServerPort = SocketUtils.findAvailableTcpPort();

    private static final Pattern MOCK_SERVER_PORT_PATTERN = Pattern.compile("\\$\\{mockServerPort}");

    private final List<String> properties;

    MockServerPropertyCustomizer(String... properties) {
        this.properties = Arrays.asList(properties);
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        context.getEnvironment().getPropertySources().addLast(
            new MockPropertySource().withProperty("mockServerPort", mockServerPort)
        );

        properties.forEach(property -> {
            String replacement = MOCK_SERVER_PORT_PATTERN.matcher(property).replaceAll(String.valueOf(mockServerPort));

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, replacement);
        });
    }
}
