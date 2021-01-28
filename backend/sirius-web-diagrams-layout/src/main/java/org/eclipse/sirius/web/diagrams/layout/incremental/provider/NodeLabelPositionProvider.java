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

import org.eclipse.sirius.web.diagrams.NodeType;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Position.Builder;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.LabelLayoutData;
import org.eclipse.sirius.web.diagrams.layout.incremental.data.NodeLayoutData;

/**
 * Provides the position to apply to a Node Label.
 *
 * @author wpiers
 */
public class NodeLabelPositionProvider {

    public Position getPosition(NodeLayoutData node, LabelLayoutData label) {
        double x = (node.getSize().getWidth() - label.getTextBounds().getSize().getWidth()) / 2;
        Builder builder = Position.newPosition().x(x);
        if (NodeType.NODE_IMAGE.equals(node.getNodeType())) {
            builder.y(-(label.getTextBounds().getSize().getHeight() + 5));
        } else if (NodeType.NODE_RECTANGLE.equals(node.getNodeType())) {
            builder.y(5);
        } else {
            builder.y(0);
        }
        return builder.build();
    }
}
