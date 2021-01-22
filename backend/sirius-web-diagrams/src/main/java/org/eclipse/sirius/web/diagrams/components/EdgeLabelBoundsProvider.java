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
import java.util.Optional;

import org.eclipse.sirius.web.diagrams.Label;
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
    public Position getPosition(Optional<Label> optionalPreviousLabel, TextBounds textBounds, String type) {
        Position position;
        if (optionalPreviousLabel.isPresent()) {
            // The position computed by the layout stays the most relevant
            position = optionalPreviousLabel.get().getPosition();
        } else if (this.routingPoints.size() < 2) {
            position = Position.UNDEFINED;
        } else {
            Position sourceAnchor = this.routingPoints.get(0);
            Position targetAnchor = this.routingPoints.get(this.routingPoints.size() - 1);
            position = this.computeNewPosition(textBounds, sourceAnchor, targetAnchor);
        }
        return position;
    }

    private Position computeNewPosition(TextBounds textBounds, Position sourceAnchor, Position targetAnchor) {
        // TODO manage other placements than CENTER
        double x = ((sourceAnchor.getX() + targetAnchor.getX()) / 2) - (textBounds.getSize().getWidth() / 2);
        double y = (sourceAnchor.getY() + targetAnchor.getY()) / 2;
        return Position.newPosition().x(x).y(y).build();
    }

    @Override
    public Position getAlignment(Optional<Label> optionalPreviousLabel, TextBounds textBounds, String type) {
        return optionalPreviousLabel.map(Label::getAlignment).orElse(textBounds.getAlignment());
    }

    @Override
    public Size getSize(Optional<Label> optionalPreviousLabel, TextBounds textBounds, String type) {
        return optionalPreviousLabel.map(Label::getSize).orElse(textBounds.getSize());
    }

}
