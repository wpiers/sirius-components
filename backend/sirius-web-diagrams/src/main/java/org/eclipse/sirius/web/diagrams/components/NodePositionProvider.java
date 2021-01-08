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
    }

    public Position getNextPosition() {
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
        return newPosition;
    }
}
