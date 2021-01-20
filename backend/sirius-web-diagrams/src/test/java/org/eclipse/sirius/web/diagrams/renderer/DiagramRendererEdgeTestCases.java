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
package org.eclipse.sirius.web.diagrams.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.diagrams.ArrowStyle;
import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Edge;
import org.eclipse.sirius.web.diagrams.EdgeStyle;
import org.eclipse.sirius.web.diagrams.INodeStyle;
import org.eclipse.sirius.web.diagrams.LineStyle;
import org.eclipse.sirius.web.diagrams.Node;
import org.eclipse.sirius.web.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.web.diagrams.components.DiagramComponent;
import org.eclipse.sirius.web.diagrams.components.DiagramComponentProps;
import org.eclipse.sirius.web.diagrams.description.DiagramDescription;
import org.eclipse.sirius.web.diagrams.description.EdgeDescription;
import org.eclipse.sirius.web.diagrams.description.LabelDescription;
import org.eclipse.sirius.web.diagrams.description.LabelStyleDescription;
import org.eclipse.sirius.web.diagrams.description.NodeDescription;
import org.eclipse.sirius.web.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for the rendering of the nodes in diagrams.
 *
 * @author sbegaudeau
 */
public class DiagramRendererEdgeTestCases {
    private static final UUID EDGE_DESCRIPTION_ID = UUID.randomUUID();

    private static final UUID DIAGRAM_DESCRIPTION_ID = UUID.randomUUID();

    private static final UUID NODE_DESCRIPTION_ID1 = UUID.randomUUID();

    private static final UUID NODE_DESCRIPTION_ID2 = UUID.randomUUID();

    private static final String FIRST_OBJECT_ID = "First"; //$NON-NLS-1$

    private static final String SECOND_OBJECT_ID = "Second"; //$NON-NLS-1$

    private final Logger logger = LoggerFactory.getLogger(DiagramRendererEdgeTestCases.class);

    /**
     * Creates a diagram with two nodes "First" and "Second" and with an edge between the two nodes.
     */
    @Test
    public void testSimpleEdgeRendering() {
        NodeDescription nodeDescription = this.getNodeDescription(NODE_DESCRIPTION_ID1);
        EdgeDescription edgeDescription = this.getEdgeDescription(nodeDescription);

        // @formatter:off
        DiagramDescription diagramDescription = DiagramDescription.newDiagramDescription(DIAGRAM_DESCRIPTION_ID)
                .label("") //$NON-NLS-1$
                .canCreatePredicate(variableManager -> true)
                .targetObjectIdProvider(variableManager -> "diagramTargetObjectId") //$NON-NLS-1$
                .labelProvider(variableManager -> "Diagram") //$NON-NLS-1$
                .nodeDescriptions(List.of(nodeDescription))
                .edgeDescriptions(List.of(edgeDescription))
                .toolSections(List.of())
                .build();
        // @formatter:on

        VariableManager variableManager = new VariableManager();
        DiagramComponentProps props = new DiagramComponentProps(variableManager, diagramDescription, List.of(), Optional.empty(), Map.of(), Set.of(), Optional.empty());
        Element element = new Element(DiagramComponent.class, props);
        Diagram diagram = new DiagramRenderer(this.logger).render(element);

        assertThat(diagram).isNotNull();
        assertThat(diagram.getId()).asString().isNotBlank();
        assertThat(diagram.getLabel()).isNotBlank();
        assertThat(diagram.getTargetObjectId()).isNotBlank();

        assertThat(diagram.getNodes()).hasSize(2);

        Node firstNode = diagram.getNodes().get(0);
        Node secondNode = diagram.getNodes().get(1);

        assertThat(diagram.getEdges()).hasSize(1);

        Edge edge = diagram.getEdges().get(0);
        assertThat(edge).extracting(Edge::getSourceId).isEqualTo(firstNode.getId());
        assertThat(edge).extracting(Edge::getTargetId).isEqualTo(secondNode.getId());
    }

    /**
     * Check edge between nodes (of a first mapping) that represents objects that are represented by another mapping. To
     * do so, this test creates a diagram with
     * <li>two nodes "First" and "Second" from a nodeDescriptionId1</li>
     * <li>two other nodes "First" and "Second" from a nodeDescriptionId2</li>
     * <li>an edge between nodes of a type nodeDescriptionId1</li>
     */
    @Test
    public void testEdgeRendering() {
        NodeDescription nodeDescription1 = this.getNodeDescription(NODE_DESCRIPTION_ID1);
        NodeDescription nodeDescription2 = this.getNodeDescription(NODE_DESCRIPTION_ID2);
        EdgeDescription edgeDescription = this.getEdgeDescription(nodeDescription1);

        // @formatter:off
        DiagramDescription diagramDescription = DiagramDescription.newDiagramDescription(DIAGRAM_DESCRIPTION_ID)
                .label("") //$NON-NLS-1$
                .canCreatePredicate(variableManager -> true)
                .targetObjectIdProvider(variableManager -> "diagramTargetObjectId") //$NON-NLS-1$
                .labelProvider(variableManager -> "Diagram") //$NON-NLS-1$
                .nodeDescriptions(List.of(nodeDescription1, nodeDescription2))
                .edgeDescriptions(List.of(edgeDescription))
                .toolSections(List.of())
                .build();
        // @formatter:on

        VariableManager variableManager = new VariableManager();
        DiagramComponentProps props = new DiagramComponentProps(variableManager, diagramDescription, List.of(), Optional.empty(), Map.of(), Set.of(), Optional.empty());
        Element element = new Element(DiagramComponent.class, props);
        Diagram diagram = new DiagramRenderer(this.logger).render(element);

        assertThat(diagram.getNodes()).hasSize(4);

        Node node1 = diagram.getNodes().get(0);
        Node node2 = diagram.getNodes().get(1);

        assertThat(diagram.getEdges()).hasSize(1);

        Edge edge = diagram.getEdges().get(0);
        assertThat(edge).extracting(Edge::getSourceId).isEqualTo(node1.getId());
        assertThat(edge).extracting(Edge::getTargetId).isEqualTo(node2.getId());
    }

    private NodeDescription getNodeDescription(UUID nodeDescriptionId) {
        // @formatter:off
        LabelStyleDescription labelStyleDescription = LabelStyleDescription.newLabelStyleDescription()
                .colorProvider(variableManager -> "#000000") //$NON-NLS-1$
                .fontSizeProvider(variableManager -> 16)
                .boldProvider(variableManager -> false)
                .italicProvider(variableManager -> false)
                .underlineProvider(variableManager -> false)
                .strikeThroughProvider(variableManager -> false)
                .iconURLProvider(variableManager -> "") //$NON-NLS-1$
                .build();

        LabelDescription labelDescription = LabelDescription.newLabelDescription("labelDescriptionId") //$NON-NLS-1$
                .idProvider(variableManager -> "labelId") //$NON-NLS-1$
                .textProvider(variableManager -> "Node") //$NON-NLS-1$
                .styleDescription(labelStyleDescription)
                .build();

        Function<VariableManager, INodeStyle> nodeStyleProvider = variableManager -> {
            return RectangularNodeStyle.newRectangularNodeStyle()
                    .color("") //$NON-NLS-1$
                    .borderColor("") //$NON-NLS-1$
                    .borderSize(0)
                    .borderStyle(LineStyle.Solid)
                    .build();
        };

        Function<VariableManager, String> targetObjectIdProvider = variableManager -> {
            Object object = variableManager.getVariables().get(VariableManager.SELF);
            if (object instanceof String) {
                return nodeDescriptionId + "__" +  object; //$NON-NLS-1$
            }
            return null;
        };

        return NodeDescription.newNodeDescription(nodeDescriptionId)
                .typeProvider(variableManager -> "") //$NON-NLS-1$
                .semanticElementsProvider(variableManager -> List.of(FIRST_OBJECT_ID, SECOND_OBJECT_ID))
                .targetObjectIdProvider(targetObjectIdProvider)
                .targetObjectKindProvider(variableManager -> "") //$NON-NLS-1$
                .targetObjectLabelProvider(variableManager -> "")//$NON-NLS-1$
                .labelDescription(labelDescription)
                .styleProvider(nodeStyleProvider)
                .borderNodeDescriptions(new ArrayList<>())
                .childNodeDescriptions(new ArrayList<>())
                .labelEditHandler((variableManager, newLabel) -> Status.OK)
                .deleteHandler(variableManager -> Status.OK)
                .build();
        // @formatter:on
    }

    private EdgeDescription getEdgeDescription(NodeDescription nodeDescription) {
        // @formatter:off
        Function<VariableManager, List<Element>> sourceNodesProvider = variableManager -> {
            var optionalCache = variableManager.get(DiagramDescription.CACHE, DiagramRenderingCache.class);
            Map<Object, List<Element>> objectToNodes = optionalCache.map(DiagramRenderingCache::getObjectToNodes).orElse(new HashMap<>());

            List<Element> sourceNodes = objectToNodes.get(FIRST_OBJECT_ID).stream()
                    .filter(node-> ((NodeElementProps) node.getProps()).getDescriptionId().equals(nodeDescription.getId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return sourceNodes;
        };

        Function<VariableManager, List<Element>> targetNodesProvider = variableManager -> {
            var optionalCache = variableManager.get(DiagramDescription.CACHE, DiagramRenderingCache.class);
            Map<Object, List<Element>> objectToNodes = optionalCache.map(DiagramRenderingCache::getObjectToNodes).orElse(new HashMap<>());

            List<Element> targetNodes = objectToNodes.get(SECOND_OBJECT_ID).stream()
                    .filter(node-> ((NodeElementProps) node.getProps()).getDescriptionId().equals(nodeDescription.getId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return targetNodes;
        };

        Function<VariableManager, EdgeStyle> edgeStyleProvider = variableManager -> {
            return EdgeStyle.newEdgeStyle()
                    .size(2)
                    .lineStyle(LineStyle.Dash_Dot)
                    .sourceArrow(ArrowStyle.InputArrowWithDiamond)
                    .targetArrow(ArrowStyle.None)
                    .color("rgb(1, 2, 3)") //$NON-NLS-1$
                    .build();
        };

        Function<VariableManager, String> idProvider = variableManager -> {
            return variableManager.get(VariableManager.SELF, String.class).orElse(null);
        };


        return EdgeDescription.newEdgeDescription(EDGE_DESCRIPTION_ID)
                .semanticElementsProvider(variableManager -> List.of(FIRST_OBJECT_ID))
                .sourceNodesProvider(sourceNodesProvider)
                .targetNodesProvider(targetNodesProvider)
                .sourceNodeDescriptions(List.of(nodeDescription))
                .targetNodeDescriptions(List.of(nodeDescription))
                .targetObjectIdProvider(idProvider)
                .targetObjectKindProvider(variableManager -> "") //$NON-NLS-1$
                .targetObjectLabelProvider(variableManager -> "")//$NON-NLS-1$
                .styleProvider(edgeStyleProvider)
                .deleteHandler(variableManager -> Status.ERROR)
                .build();
        // @formatter:on
    }
}
