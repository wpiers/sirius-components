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

import java.util.List;

import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.TextBounds;

/**
 * Provides the bounds (Alignment, Size and Position) to apply to a new Edge Label.
 *
 * @author wpiers
 */
public class EdgeLabelBoundsProvider implements ILabelBoundsProvider {

    /**
     * The edge routing points.
     */
    private List<Position> routingPoints;

    /**
     * Constructor for an edge.
     *
     * @param routingPoints
     */
    public EdgeLabelBoundsProvider(List<Position> routingPoints) {
        this.routingPoints = routingPoints;
    }

    @Override
    public Position getPosition(TextBounds textBounds, String type) {
        if (this.routingPoints.size() < 2) {
            return Position.UNDEFINED;
        }
        Position sourceAnchor = this.routingPoints.get(0);
        Position targetAnchor = this.routingPoints.get(this.routingPoints.size() - 1);

        // TODO manage other placements than CENTER
        double x = (sourceAnchor.getX() + targetAnchor.getX()) / 2;
        double y = (sourceAnchor.getY() + targetAnchor.getY()) / 2;
        return Position.newPosition().x(x).y(y).build();
    }

    @Override
    public Position getAlignment(TextBounds textBounds, String type) {
        return textBounds.getAlignment();
    }

    @Override
    public Size getSize(TextBounds textBounds, String type) {
        return textBounds.getSize();
    }

}
