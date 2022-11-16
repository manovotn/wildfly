package org.wildfly.extension.microprofile.telemetry;

import static org.jboss.as.controller.PersistentResourceXMLDescription.builder;

import org.jboss.as.controller.PersistentResourceXMLDescription;
import org.jboss.as.controller.PersistentResourceXMLParser;

public class MicroProfileTelemetryParser extends PersistentResourceXMLParser {
    private final MicroProfileTelemetrySchema schema;
    public MicroProfileTelemetryParser(MicroProfileTelemetrySchema schema) {
        this.schema = schema;
    }

    @Override
    public PersistentResourceXMLDescription getParserDescription() {
        return builder(MicroProfileTelemetryExtension.SUBSYSTEM_PATH, schema.getNamespaceUri())
//                .addAttributes(MicroProfileTelemetrySubsystemDefinition.ATTRIBUTES)
                .build();
    }
}
