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

import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.DiagramLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.EdgeLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.IContainerLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.NodeLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.provider.EdgeLabelPositionProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.provider.EdgeRoutingPointsProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.provider.NodeLabelPositionProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.provider.NodePositionProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.provider.NodeSizeProvider;
import org.eclipse.sirius.web.diagrams.layout.incremental.updater.ContainmentUpdater;
import org.eclipse.sirius.web.diagrams.layout.incremental.updater.OverlapsUpdater;

/**
 * The engine that computes the incremental layout, using informations from:
 * <ul>
 * <li>the UI: created nodes, moved nodes...</li>
 * <li>the existing layout data</li>
 * <ul>
 *
 * @author wpiers
 */
public class IncrementalLayoutEngine {

    /**
     * The minimal distance between nodes.
     */
    public static final double NODES_GAP = 30;

    private Map<String, Position> newPositionsMap;

    private Map<String, Size> newSizesMap;

    private NodeLabelPositionProvider nodeLabelPositionProvider;

    private EdgeRoutingPointsProvider edgeRoutingPointsProvider;

    private EdgeLabelPositionProvider edgeLabelPositionProvider;

    private NodeSizeProvider nodeSizeProvider;

    private NodePositionProvider nodePositionProvider;

    public IncrementalLayoutEngine(Map<String, Position> newPositionsMap, Map<String, Size> newSizesMap, Optional<Position> optionalStartingPosition) {
        this.newPositionsMap = newPositionsMap;
        this.newSizesMap = newSizesMap;
        this.edgeRoutingPointsProvider = new EdgeRoutingPointsProvider();
        this.nodeLabelPositionProvider = new NodeLabelPositionProvider();
        this.edgeLabelPositionProvider = new EdgeLabelPositionProvider();
        this.nodeSizeProvider = new NodeSizeProvider();
        this.nodePositionProvider = new NodePositionProvider(optionalStartingPosition);
    }

    public void layout(DiagramLayoutData diagram) {
        // first we layout all the nodes
        for (NodeLayoutData node : diagram.getChildNodes()) {
            this.layoutNode(node);
        }

        // resolve overlaps due to previous changes
        new OverlapsUpdater().update(diagram);

        // resize according to the content
        new ContainmentUpdater().update(diagram);

        // finally we recompute the edges that needs to
        for (EdgeLayoutData edge : diagram.getEdges()) {
            if (this.hasChanged(edge.getSource()) || this.hasChanged(edge.getTarget())) {
                this.layoutEdge(edge);
            }
        }
    }

    private void layoutNode(NodeLayoutData node) {
        // first layout border & child nodes
        for (NodeLayoutData borderNode : node.getBorderNodes()) {
            this.layoutNode(borderNode);
        }
        for (NodeLayoutData childNode : node.getChildNodes()) {
            this.layoutNode(childNode);
        }

        // recompute the node size according to what has been done in the previous steps
        Size newSize = this.newSizesMap.get(node.getId());
        if (newSize != null) {
            node.setSize(newSize);
            node.setChanged(true);
        } else if ((node.getSize().getHeight() == -1 && node.getSize().getWidth() == -1)) {
            node.setSize(this.nodeSizeProvider.getSize(node.getStyle()));
            node.setChanged(true);
        }

        // recompute the node position
        Position newPosition = this.newPositionsMap.get(node.getId());
        if (newPosition != null) {
            node.setPosition(newPosition);
            node.setChanged(true);
            node.setPinned(true);
        } else if (node.getPosition().getX() == -1 && node.getPosition().getY() == -1) {
            node.setPosition(this.nodePositionProvider.getPosition(node));
            node.setChanged(true);
            node.setPinned(true);
        }

        // resolve overlaps due to previous changes
        new OverlapsUpdater().update(node);

        // resize / change position according to the content
        new ContainmentUpdater().update(node);

        // recompute the label
        if (node.getLabel() != null) {
            node.getLabel().setPosition(this.nodeLabelPositionProvider.getPosition(node, node.getLabel()));
        }
    }

    private void layoutEdge(EdgeLayoutData edge) {
        // recompute the edge bendpoints
        edge.setRoutingPoints(this.edgeRoutingPointsProvider.getRoutingPoints(edge));

        // recompute edge labels
        if (edge.getCenterLabel() != null) {
            edge.getCenterLabel().setPosition(this.edgeLabelPositionProvider.getCenterPosition(edge, edge.getCenterLabel()));
        }
    }

    /**
     * States whether or not a node has changed (size and/or position). This indicates that the related edges must be
     * recomputed.
     *
     * @param node
     *            the node
     * @return <true> if the node has moved / been resized
     */
    private boolean hasChanged(NodeLayoutData node) {
        boolean res = false;
        if (node.hasChanged()) {
            res = true;
        } else {
            IContainerLayoutData parent = node.getParent();
            if (parent instanceof NodeLayoutData) {
                res = this.hasChanged((NodeLayoutData) parent);
            }
        }
        return res;
    }

}
