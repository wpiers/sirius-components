/*******************************************************************************
 * Copyright (c) 2021 Thales.
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
package org.eclipse.sirius.web.diagrams.layout.incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Edge;
import org.eclipse.sirius.web.diagrams.Label;
import org.eclipse.sirius.web.diagrams.Node;
import org.eclipse.sirius.web.diagrams.TextBounds;
import org.eclipse.sirius.web.diagrams.TextBoundsProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.DiagramLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.EdgeLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.IContainerLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.ILayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.LabelLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.NodeLayoutData;

/**
 * Used to convert the diagram into layout data. During the transformation.
 *
 * @author wpiers
 */
public class DiagramConverter {

    public ConvertedDiagram convert(Diagram diagram) {
        Map<String, ILayoutData> id2LayoutData = new HashMap<>();

        DiagramLayoutData layoutData = new DiagramLayoutData();
        String id = diagram.getId().toString();
        layoutData.setId(id);
        id2LayoutData.put(id, layoutData);

        layoutData.setPosition(diagram.getPosition());
        layoutData.setSize(diagram.getSize());

        List<NodeLayoutData> nodes = new ArrayList<>();
        for (Node node : diagram.getNodes()) {
            nodes.add(this.convertNode(node, layoutData, id2LayoutData));
        }
        layoutData.setChildNodes(nodes);

        List<EdgeLayoutData> edges = new ArrayList<>();
        for (Edge edge : diagram.getEdges()) {
            edges.add(this.convertEdge(edge, layoutData, id2LayoutData));
        }
        layoutData.setEdges(edges);

        return new ConvertedDiagram(layoutData, id2LayoutData);
    }

    private NodeLayoutData convertNode(Node node, IContainerLayoutData parent, Map<String, ILayoutData> id2LayoutData) {
        NodeLayoutData layoutData = new NodeLayoutData();
        String id = node.getId().toString();
        layoutData.setId(id);
        id2LayoutData.put(node.getId().toString(), layoutData);

        layoutData.setParent(parent);
        layoutData.setNodeType(node.getType());
        layoutData.setStyle(node.getStyle());

        layoutData.setPosition(node.getPosition());
        layoutData.setSize(node.getSize());

        List<NodeLayoutData> borderNodes = new ArrayList<>();
        for (Node borderNode : node.getBorderNodes()) {
            borderNodes.add(this.convertNode(borderNode, layoutData, id2LayoutData));
        }
        layoutData.setBorderNodes(borderNodes);

        List<NodeLayoutData> childNodes = new ArrayList<>();
        for (Node childNode : node.getChildNodes()) {
            childNodes.add(this.convertNode(childNode, layoutData, id2LayoutData));
        }
        layoutData.setChildNodes(childNodes);

        LabelLayoutData labelLayoutData = this.convertLabel(node.getLabel(), id2LayoutData);
        layoutData.setLabel(labelLayoutData);

        return layoutData;
    }

    private EdgeLayoutData convertEdge(Edge edge, DiagramLayoutData diagramLayoutData, Map<String, ILayoutData> id2LayoutData) {
        EdgeLayoutData layoutData = new EdgeLayoutData();
        String id = edge.getId().toString();
        layoutData.setId(id);
        id2LayoutData.put(edge.getId().toString(), layoutData);

        if (edge.getBeginLabel() != null) {
            layoutData.setBeginLabel(this.convertLabel(edge.getBeginLabel(), id2LayoutData));
        }
        if (edge.getCenterLabel() != null) {
            layoutData.setCenterLabel(this.convertLabel(edge.getCenterLabel(), id2LayoutData));
        }
        if (edge.getEndLabel() != null) {
            layoutData.setEndLabel(this.convertLabel(edge.getEndLabel(), id2LayoutData));
        }

        layoutData.setRoutingPoints(edge.getRoutingPoints());
        layoutData.setSource((NodeLayoutData) id2LayoutData.get(edge.getSourceId().toString()));
        layoutData.setTarget((NodeLayoutData) id2LayoutData.get(edge.getTargetId().toString()));

        return layoutData;
    }

    private LabelLayoutData convertLabel(Label label, Map<String, ILayoutData> id2LayoutData) {
        LabelLayoutData layoutData = new LabelLayoutData();
        String id = label.getId().toString();
        layoutData.setId(id);
        id2LayoutData.put(label.getId().toString(), layoutData);

        layoutData.setPosition(label.getPosition());

        TextBounds textBounds = new TextBoundsProvider().computeBounds(label.getStyle(), label.getText());
        layoutData.setTextBounds(textBounds);

        return layoutData;
    }

}
