package org.wildfly.extension.microprofile.telemetry.cdi;

import io.smallrye.opentelemetry.api.OpenTelemetryConfig;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Singleton;

public class MicroProfileTelemetryCdiExtension implements Extension {
    private final OpenTelemetryConfig serverConfig;

    public MicroProfileTelemetryCdiExtension(OpenTelemetryConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void registerOpenTelemetryConfigBean(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
        System.out.println("Registering server config: " + serverConfig.getClass());
        abd.addBean()
                .scope(Singleton.class)
                .addQualifier(Default.Literal.INSTANCE)
                .types(OpenTelemetryConfig.class)
                .addTransitiveTypeClosure(OpenTelemetryConfig.class)
                .createWith(e -> {
                    System.out.println("hi");
                    return serverConfig;
                });

/*
        abd.addBean()
                .scope(Singleton.class)
                .addQualifier(Default.Literal.INSTANCE)
//                .types(OpenTelemetryConfig.class)
                .addTransitiveTypeClosure(OpenTelemetryConfig.class)
                .produceWith(c -> {
                            Config appConfig = beanManager.createInstance().select(Config.class).get();
                            Map<String, String> properties = new HashMap<>(serverConfig.properties());
                            for (String propertyName : appConfig.getPropertyNames()) {
                                if (propertyName.startsWith("otel.") || propertyName.startsWith("OTEL_")) {
                                    appConfig.getOptionalValue(propertyName, String.class).ifPresent(
                                            value -> properties.put(propertyName, value));
                                }
                            }
                            return properties;
                        }
                );
*/
    }
}
