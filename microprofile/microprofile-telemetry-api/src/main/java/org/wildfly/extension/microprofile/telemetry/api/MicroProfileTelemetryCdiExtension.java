package org.wildfly.extension.microprofile.telemetry.api;

import java.util.HashMap;
import java.util.Map;

import io.smallrye.opentelemetry.api.OpenTelemetryConfig;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.Config;

public class MicroProfileTelemetryCdiExtension implements Extension {
    private final Map<String, String> serverConfig;

    public MicroProfileTelemetryCdiExtension(Map<String, String> serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void registerOpenTelemetryConfigBean(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
        System.out.println("Registering server config: " + serverConfig.getClass());

        abd.addBean()
                .scope(Singleton.class)
                .addQualifier(Default.Literal.INSTANCE)
                .types(OpenTelemetryConfig.class)
                .addTransitiveTypeClosure(OpenTelemetryConfig.class)
                .createWith(c -> {
                            System.out.println("Creating!");
                            Config appConfig = beanManager.createInstance().select(Config.class).get();
                            Map<String, String> properties = new HashMap<>(serverConfig);
                            for (String propertyName : appConfig.getPropertyNames()) {
                                if (propertyName.startsWith("otel.") || propertyName.startsWith("OTEL_")) {
                                    appConfig.getOptionalValue(propertyName, String.class).ifPresent(
                                            value -> properties.put(propertyName, value));
                                }
                            }
                            return properties;
                        }
                );
    }
}
