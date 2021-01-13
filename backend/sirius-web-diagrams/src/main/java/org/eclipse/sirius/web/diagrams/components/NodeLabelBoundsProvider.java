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
 * Provides the bounds (Size and Position) to apply to a new Node Label.
 *
 * @author wpiers
 */
public class NodeLabelBoundsProvider {

    /** The Spacing between a label and the node. */
    private static final int LABEL_Y_SPACING = 5;

    public Position getPosition(TextBounds textBounds, String parentType, Size parentSize) {
        double x = (parentSize.getWidth() - textBounds.getSize().getWidth()) / 2;
        Builder builder = Position.newPosition().x(x);
        if (NodeType.NODE_IMAGE.equals(parentType)) {
            builder.y(-(textBounds.getSize().getHeight() + LABEL_Y_SPACING));
        } else if (NodeType.NODE_RECTANGLE.equals(parentType)) {
            builder.y(LABEL_Y_SPACING);
        } else {
            builder.y(0);
        }
        return builder.build();
    }

    public Position getAlignment(TextBounds textBounds) {
        return textBounds.getAlignment();
    }

    public Size getSize(TextBounds textBounds) {
        return textBounds.getSize();
    }

}
