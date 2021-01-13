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

import org.eclipse.sirius.web.diagrams.NodeType;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Position.Builder;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.TextBounds;

/**
 * Provides the bounds (Alignment, Size and Position) to apply to a new Label.
 *
 * @author wpiers
 */
public class LabelBoundsProvider {

    /** The Spacing between a label and the element to describe. */
    private static final int LABEL_Y_SPACING = 5;

    private String parentNodeType;

    private Size parentNodeSize;

    /**
     * Constructor for an edge.
     */
    public LabelBoundsProvider() {
    }

    /**
     * Constructor for a node.
     */
    public LabelBoundsProvider(String parentType, Size parentSize) {
        this.parentNodeType = parentType;
        this.parentNodeSize = parentSize;
    }

    public Position getPosition(TextBounds textBounds, String type) {
        if (this.parentNodeType == null) {
            return Position.UNDEFINED;
        }
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

    public Position getAlignment(TextBounds textBounds, String type) {
        return textBounds.getAlignment();
    }

    public Size getSize(TextBounds textBounds, String type) {
        return textBounds.getSize();
    }

}
