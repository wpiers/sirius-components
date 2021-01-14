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

import org.eclipse.sirius.web.diagrams.Position;
import org.junit.Test;

/**
 * Test cases for {@link NodePositionProvider}.
 *
 * @author fbarbin
 */
public class NodePositionProviderTestCases {

    private static final double STARTX = 20;

    private static final double STARTY = 70;

    @Test
    public void testNodePosition() {
        NodePositionProvider nodePositionProvider = new NodePositionProvider(STARTX, STARTY);
        Position nextPosition = nodePositionProvider.getNextPosition(Optional.empty());
        assertThat(nextPosition).extracting(Position::getX).isEqualTo(Double.valueOf(STARTX));
        assertThat(nextPosition).extracting(Position::getY).isEqualTo(Double.valueOf(STARTY));

        nextPosition = nodePositionProvider.getNextPosition(Optional.empty());
        assertThat(nextPosition).extracting(Position::getX).isEqualTo(Double.valueOf(STARTX + 30));
        assertThat(nextPosition).extracting(Position::getY).isEqualTo(Double.valueOf(STARTY + 30));
    }

}
