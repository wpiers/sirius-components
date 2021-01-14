/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.web.spring.collaborative.diagrams;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.eclipse.sirius.web.collaborative.api.services.Monitoring;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.ViewCreationRequest;
import org.eclipse.sirius.web.diagrams.components.DiagramComponent;
import org.eclipse.sirius.web.diagrams.components.DiagramComponentProps;
import org.eclipse.sirius.web.diagrams.description.DiagramDescription;
import org.eclipse.sirius.web.diagrams.layout.api.ILayoutService;
import org.eclipse.sirius.web.diagrams.renderer.DiagramRenderer;
import org.eclipse.sirius.web.representations.VariableManager;
import org.eclipse.sirius.web.services.api.objects.IEditingContext;
import org.eclipse.sirius.web.services.api.objects.IObjectService;
import org.eclipse.sirius.web.services.api.representations.IRepresentationDescriptionService;
import org.eclipse.sirius.web.services.api.representations.IRepresentationService;
import org.eclipse.sirius.web.services.api.representations.RepresentationDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Service used to create diagrams.
 *
 * @author sbegaudeau
 */
@Service
public class DiagramCreationService implements IDiagramCreationService {

    private final IRepresentationDescriptionService representationDescriptionService;

    private final IRepresentationService representationService;

    private final IObjectService objectService;

    private final ILayoutService layoutService;

    private final Timer timer;

    private final Logger logger = LoggerFactory.getLogger(DiagramCreationService.class);

    public DiagramCreationService(IRepresentationDescriptionService representationDescriptionService, IRepresentationService representationService, IObjectService objectService,
            ILayoutService layoutService, MeterRegistry meterRegistry) {
        this.representationDescriptionService = Objects.requireNonNull(representationDescriptionService);
        this.representationService = Objects.requireNonNull(representationService);
        this.objectService = Objects.requireNonNull(objectService);
        this.layoutService = Objects.requireNonNull(layoutService);
        // @formatter:off
        this.timer = Timer.builder(Monitoring.REPRESENTATION_EVENT_PROCESSOR_REFRESH)
                .tag(Monitoring.NAME, "diagram") //$NON-NLS-1$
                .register(meterRegistry);
        // @formatter:on
    }

    @Override
    public Diagram create(String label, Object targetObject, DiagramDescription diagramDescription, IEditingContext editingContext) {
        Diagram newDiagram = this.doRender(label, targetObject, editingContext, diagramDescription, List.of(), Optional.empty());
        // For now, we layout the diagram at creation time only if the automatic layout at the refresh time has been
        // deactivated.
        if (Boolean.getBoolean("sirius.layout.auto.deactivate")) { //$NON-NLS-1$
            newDiagram = this.layoutService.layout(newDiagram);
        }
        return newDiagram;
    }

    @Override
    public Optional<Diagram> refresh(IEditingContext editingContext, IDiagramContext diagramContext) {
        Diagram previousDiagram = diagramContext.getDiagram();
        var optionalObject = this.objectService.getObject(editingContext, previousDiagram.getTargetObjectId());
        // @formatter:off
        var optionalDiagramDescription = this.representationDescriptionService.findRepresentationDescriptionById(previousDiagram.getDescriptionId())
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast);
        // @formatter:on

        if (optionalObject.isPresent() && optionalDiagramDescription.isPresent()) {
            Object object = optionalObject.get();
            DiagramDescription diagramDescription = optionalDiagramDescription.get();
            List<ViewCreationRequest> viewCreationRequests = diagramContext.getViewCreationRequests();

            Diagram diagram = this.doRender(previousDiagram.getLabel(), object, editingContext, diagramDescription, viewCreationRequests, Optional.of(previousDiagram));
            return Optional.of(diagram);
        }
        return Optional.empty();
    }

    private Diagram doRender(String label, Object targetObject, IEditingContext editingContext, DiagramDescription diagramDescription, List<ViewCreationRequest> viewCreationRequests,
            Optional<Diagram> optionalPreviousDiagram) {

        long start = System.currentTimeMillis();

        VariableManager variableManager = new VariableManager();
        variableManager.put(DiagramDescription.LABEL, label);
        variableManager.put(VariableManager.SELF, targetObject);
        variableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);

        DiagramComponentProps props = new DiagramComponentProps(variableManager, diagramDescription, viewCreationRequests, optionalPreviousDiagram);
        Element element = new Element(DiagramComponent.class, props);

        Diagram newDiagram = new DiagramRenderer(this.logger).render(element);
        if (!Boolean.getBoolean("sirius.layout.auto.deactivate")) { //$NON-NLS-1$
            newDiagram = this.layoutService.layout(newDiagram);
        }
        RepresentationDescriptor representationDescriptor = this.getRepresentationDescriptor(editingContext.getProjectId(), newDiagram);
        this.representationService.save(representationDescriptor);

        long end = System.currentTimeMillis();
        this.timer.record(end - start, TimeUnit.MILLISECONDS);
        return newDiagram;
    }

    private RepresentationDescriptor getRepresentationDescriptor(UUID projectId, Diagram diagram) {
        // @formatter:off
        return RepresentationDescriptor.newRepresentationDescriptor(diagram.getId())
                .projectId(projectId)
                .descriptionId(diagram.getDescriptionId())
                .targetObjectId(diagram.getTargetObjectId())
                .label(diagram.getLabel())
                .representation(diagram)
                .build();
        // @formatter:on
    }

}
