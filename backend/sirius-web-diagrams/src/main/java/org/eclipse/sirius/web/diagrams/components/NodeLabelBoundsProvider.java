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

import java.util.Optional;

import org.eclipse.sirius.web.diagrams.Label;
import org.eclipse.sirius.web.diagrams.NodeType;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Position.Builder;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.TextBounds;

/**
 * Provides the bounds (Alignment, Size and Position) to apply to a new Node Label.
 *
 * @author wpiers
 */
public class NodeLabelBoundsProvider implements ILabelBoundsProvider {

    private String parentNodeType;

    private Size parentNodeSize;

    /**
     * Constructor for a node.
     */
    public NodeLabelBoundsProvider(String parentType, Size parentSize) {
        this.parentNodeType = parentType;
        this.parentNodeSize = parentSize;
    }

    @Override
    public Position getPosition(Optional<Label> optionalPreviousLabel, TextBounds textBounds, String type) {
        if (this.parentNodeType == null) {
            return Position.UNDEFINED;
        }

        // We always recompute the position according to the textbounds as the text might have changed.
        // TODO manage other placements than CENTER
        double x = (this.parentNodeSize.getWidth() - textBounds.getSize().getWidth()) / 2;
        Builder builder = Position.newPosition().x(x);
        if (NodeType.NODE_IMAGE.equals(this.parentNodeType)) {
            builder.y(-(textBounds.getSize().getHeight() + LABEL_Y_SPACING));
        } else if (NodeType.NODE_RECTANGLE.equals(this.parentNodeType)) {
            builder.y(LABEL_Y_SPACING);
        } else {
            builder.y(0);
        }
        return builder.build();
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
