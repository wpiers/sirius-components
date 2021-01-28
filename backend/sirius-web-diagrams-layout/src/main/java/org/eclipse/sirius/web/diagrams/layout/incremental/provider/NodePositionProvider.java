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
package org.eclipse.sirius.web.diagrams.layout.incremental.provider;

import java.util.Optional;

import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.layout.incremental.IncrementalLayoutEngine;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.IContainerLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.NodeLayoutData;

/**
 * Provides the position to apply to a Node.
 *
 * @author wpiers
 */
public class NodePositionProvider {

    /**
     * The position where to create a node without existing position. Used to support the creation tool.
     */
    private Optional<Position> optionalStartingPosition;

    /**
     * The last node for which we computed a position. Used when computing several new positions during one refresh.
     */
    private NodeLayoutData last;

    public NodePositionProvider(Optional<Position> optionalStartingPosition) {
        this.optionalStartingPosition = optionalStartingPosition;
    }

    public Position getPosition(NodeLayoutData node) {
        Position position;
        if (this.optionalStartingPosition.isPresent() && this.last == null) {
            // The node has been created by a tool and has a fixed position
            position = this.optionalStartingPosition.get();
            this.last = node;
        } else {
            // The node has been created along with others, by a tool or a refresh
            position = this.getNewPosition(node);
        }
        return position;
    }

    private Position getNewPosition(NodeLayoutData node) {
        Position lastPosition;
        Size lastSize;
        if (this.last == null) {
            lastPosition = this.findEmptySpot(node.getParent());
            lastSize = Size.newSize().width(0).height(0).build();
        } else {
            lastPosition = this.last.getPosition();
            lastSize = this.last.getSize();
        }

        // @formatter:off
        Position newPosition = Position.newPosition()
              .x(lastPosition.getX())
              .y(lastPosition.getY() + lastSize.getHeight() + IncrementalLayoutEngine.NODES_GAP)
              .build();
        // @formatter:on
        this.last = node;
        return newPosition;
    }

    private Position findEmptySpot(IContainerLayoutData parent) {
        double bottom = IncrementalLayoutEngine.NODES_GAP;
        for (NodeLayoutData node : parent.getChildNodes()) {
            bottom = Math.max(bottom, (node.getPosition().getY() + node.getSize().getHeight() + IncrementalLayoutEngine.NODES_GAP));
        }
        return Position.newPosition().x(0).y(bottom).build();
    }
}
