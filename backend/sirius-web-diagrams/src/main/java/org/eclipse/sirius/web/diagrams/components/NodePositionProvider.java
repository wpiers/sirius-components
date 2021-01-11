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
package org.eclipse.sirius.web.diagrams.components;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Node;
import org.eclipse.sirius.web.diagrams.Position;

/**
 * Provides the Position to apply to a new node.
 *
 * @author fbarbin
 */
public class NodePositionProvider {

    private static final int NEXT_POSITION_DELTA = 30;

    private Position startingPosition;

    private Position lastPosition;

    private Map<UUID, Position> movedElementIdToNewPositionMap;

    /**
     * Default constructor.
     *
     * @param x
     *            the x coordinate of the new element starting position.
     * @param y
     *            the y coordinate of the new element starting position.
     */
    public NodePositionProvider(double x, double y) {
        // @formatter:off
        this.startingPosition = Position.newPosition()
          .x(x)
          .y(y)
          .build();
        // @formatter:on
        this.movedElementIdToNewPositionMap = Map.of();
    }

    /**
     * Default constructor.
     *
     * @param x
     *            the x coordinate of the new element starting position.
     * @param y
     *            the y coordinate of the new element starting position.
     * @param movedElementIdToNewPositionMap
     *            a map containing the new position of diagram elements (identified by their UUID)
     */
    public NodePositionProvider(double x, double y, Map<UUID, Position> movedElementIdToNewPositionMap) {
        this(x, y);
        this.movedElementIdToNewPositionMap = Map.copyOf(movedElementIdToNewPositionMap);
    }

    public Optional<Position> getMovedPosition(UUID elementId) {
        return Optional.ofNullable(this.movedElementIdToNewPositionMap.get(elementId));
    }

    public Position getNextPosition(Optional<Object> previousParentElement) {
        Position newPosition;
        if (this.lastPosition == null) {
            this.lastPosition = this.startingPosition;
            newPosition = this.lastPosition;
        } else {
            // @formatter:off
            newPosition = Position.newPosition()
              .x(this.lastPosition.getX() + NEXT_POSITION_DELTA)
              .y(this.lastPosition.getY() + NEXT_POSITION_DELTA)
              .build();
            // @formatter:on
            this.lastPosition = newPosition;
        }

        // Shift the new position if necessary, according to existing elements
        if (previousParentElement.isPresent()) {
            Object element = previousParentElement.get();
            Collection<Node> siblings;
            if (element instanceof Diagram) {
                siblings = ((Diagram) element).getNodes();
            } else if (element instanceof Node) {
                siblings = ((Node) element).getChildNodes();
            } else {
                siblings = Collections.emptyList();
            }
            if (!siblings.isEmpty()) {
                while (this.isOccupied(siblings, newPosition)) {
                    newPosition = Position.newPosition().x(newPosition.getX() + NEXT_POSITION_DELTA).y(newPosition.getY() + NEXT_POSITION_DELTA).build();
                }
            }
        }
        return newPosition;
    }

    private boolean isOccupied(Collection<Node> nodes, Position position) {
        for (Node node : nodes) {
            if (node.getPosition().getX() == position.getX() && node.getPosition().getY() == position.getY()) {
                return true;
            }
        }
        return false;
    }
}
