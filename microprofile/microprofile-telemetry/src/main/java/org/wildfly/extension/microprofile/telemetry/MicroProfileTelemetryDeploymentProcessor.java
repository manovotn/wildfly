package org.wildfly.extension.microprofile.telemetry;

import static org.jboss.as.weld.Capabilities.WELD_CAPABILITY_NAME;
import static org.wildfly.extension.microprofile.telemetry.MicroProfileTelemetryExtensionLogger.MPTEL_LOGGER;

import java.util.function.Supplier;

import io.smallrye.opentelemetry.api.OpenTelemetryConfig;
import org.jboss.as.controller.capability.CapabilityServiceSupport;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.weld.WeldCapability;
import org.wildfly.extension.microprofile.telemetry.api.MicroProfileTelemetryCdiExtension;

public class MicroProfileTelemetryDeploymentProcessor implements DeploymentUnitProcessor {
    @Override
    public void deploy(DeploymentPhaseContext deploymentPhaseContext) throws DeploymentUnitProcessingException {
        MPTEL_LOGGER.activatingSubsystem();

        final DeploymentUnit deploymentUnit = deploymentPhaseContext.getDeploymentUnit();
        if (DeploymentTypeMarker.isType(DeploymentType.EAR, deploymentUnit)) {
            return;
        }

        try {
            final CapabilityServiceSupport support = deploymentUnit.getAttachment(Attachments.CAPABILITY_SERVICE_SUPPORT);
            final WeldCapability weldCapability = support.getCapabilityRuntimeAPI(WELD_CAPABILITY_NAME, WeldCapability.class);
            if (!weldCapability.isPartOfWeldDeployment(deploymentUnit)) {
                MPTEL_LOGGER.debug("The deployment does not have Jakarta Contexts and Dependency Injection enabled. " +
                        "Skipping MicroProfile Telemetry integration.");
                return;
            }

            final OpenTelemetryConfig serverConfig =
                    (OpenTelemetryConfig) support.getCapabilityRuntimeAPI("org.wildfly.extension.opentelemetry.config",
                            Supplier.class).get();
            weldCapability.registerExtensionInstance(new MicroProfileTelemetryCdiExtension(serverConfig.properties()),
                    deploymentUnit);
        } catch (CapabilityServiceSupport.NoSuchCapabilityException e) {
            throw MPTEL_LOGGER.deploymentRequiresCapability(deploymentPhaseContext.getDeploymentUnit().getName(),
                    WELD_CAPABILITY_NAME);
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }
}
