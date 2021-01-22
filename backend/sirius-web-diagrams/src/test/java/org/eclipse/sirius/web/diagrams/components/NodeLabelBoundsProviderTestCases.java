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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.eclipse.sirius.web.diagrams.LabelStyle;
import org.eclipse.sirius.web.diagrams.NodeType;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.TextBounds;
import org.eclipse.sirius.web.diagrams.TextBoundsProvider;
import org.junit.Test;

/**
 * Test cases for {@link NodeLabelBoundsProvider}.
 *
 * @author wpiers
 */
public class NodeLabelBoundsProviderTestCases {

    private static final int FONT_SIZE = 16;

    private static final String LABEL_COLOR = "#000000"; //$NON-NLS-1$

    private static final String ICON_URL = ""; //$NON-NLS-1$

    private static final String TEST_LABEL = "My Label"; //$NON-NLS-1$

    private static final Size DEFAULT_NODE_SIZE = Size.newSize().height(70).width(150).build();

    @Test
    public void testNodeImageLabelBoundsPosition() {
        NodeLabelBoundsProvider labelBoundsProvider = new NodeLabelBoundsProvider(NodeType.NODE_IMAGE, DEFAULT_NODE_SIZE);
        LabelStyle labelStyle = LabelStyle.newLabelStyle().color(LABEL_COLOR).fontSize(FONT_SIZE).iconURL(ICON_URL).build();
        TextBounds textBounds = new TextBoundsProvider().computeBounds(labelStyle, TEST_LABEL);

        Position position = labelBoundsProvider.getPosition(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(position).extracting(Position::getX).isEqualTo(Double.valueOf(42.5390625));
        assertThat(position).extracting(Position::getY).isEqualTo(Double.valueOf(-23.3984375));

        Position alignment = labelBoundsProvider.getAlignment(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(alignment).extracting(Position::getX).isEqualTo(Double.valueOf(0));
        assertThat(alignment).extracting(Position::getY).isEqualTo(Double.valueOf(14.484375));

        Size size = labelBoundsProvider.getSize(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(size).extracting(Size::getWidth).isEqualTo(Double.valueOf(64.921875));
        assertThat(size).extracting(Size::getHeight).isEqualTo(Double.valueOf(18.3984375));
    }

    @Test
    public void testNodeRectangleLabelBoundsPosition() {
        NodeLabelBoundsProvider labelBoundsProvider = new NodeLabelBoundsProvider(NodeType.NODE_RECTANGLE, DEFAULT_NODE_SIZE);
        LabelStyle labelStyle = LabelStyle.newLabelStyle().color(LABEL_COLOR).fontSize(FONT_SIZE).iconURL(ICON_URL).build();
        TextBounds textBounds = new TextBoundsProvider().computeBounds(labelStyle, TEST_LABEL);

        Position position = labelBoundsProvider.getPosition(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(position).extracting(Position::getX).isEqualTo(Double.valueOf(42.5390625));
        assertThat(position).extracting(Position::getY).isEqualTo(Double.valueOf(5));

        Position alignment = labelBoundsProvider.getAlignment(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(alignment).extracting(Position::getX).isEqualTo(Double.valueOf(0));
        assertThat(alignment).extracting(Position::getY).isEqualTo(Double.valueOf(14.484375));

        Size size = labelBoundsProvider.getSize(Optional.empty(), textBounds, LabelPlacementKind.INSIDE_CENTER.getValue());
        assertThat(size).extracting(Size::getWidth).isEqualTo(Double.valueOf(64.921875));
        assertThat(size).extracting(Size::getHeight).isEqualTo(Double.valueOf(18.3984375));
    }

}
