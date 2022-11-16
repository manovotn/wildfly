package org.wildfly.extension.microprofile.telemetry;

import java.util.Locale;

enum MicroProfileTelemetrySchema {
    VERSION_1_0(1, 0), // WildFly 25
    ;

    public static final MicroProfileTelemetrySchema CURRENT = VERSION_1_0;

    private final int major;
    private final int minor;

    MicroProfileTelemetrySchema(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public String getNamespaceUri() {
        return String.format(Locale.ROOT, "urn:wildfly:microprofile-telemetry:%d.%d", this.major, this.minor);
    }
}
