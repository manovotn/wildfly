package org.wildfly.extension.microprofile.telemetry;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;

public class MicroProfileTelemetrySubsystemAdd extends AbstractBoottimeAddStepHandler {
    MicroProfileTelemetrySubsystemAdd() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        super.performBoottime(context, operation, model);

        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(
                        MicroProfileTelemetryExtension.SUBSYSTEM_NAME,
                        Phase.DEPENDENCIES,
                        0x1910,
                        new MicroProfileTelemetryDependencyProcessor()
                );
                processorTarget.addDeploymentProcessor(
                        MicroProfileTelemetryExtension.SUBSYSTEM_NAME,
                        Phase.POST_MODULE,
                        0x3810, // TODO: allocate a new number in wfcore
                        new MicroProfileTelemetryDeploymentProcessor());
            }
        }, OperationContext.Stage.RUNTIME);
    }
}
