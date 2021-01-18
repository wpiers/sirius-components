/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
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
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.TextBounds;

/**
 * Used to compute the bounds of a label.
 *
 * @author wpiers
 */
public interface ILabelBoundsProvider {

    /** The Spacing between a label and the element to describe. */
    int LABEL_Y_SPACING = 5;

    /**
     * Returns the position of a label with the given parameters.
     *
     * @param textBounds
     *            the label text bounds
     * @param type
     *            the label type
     * @return the label position
     */
    Position getPosition(TextBounds textBounds, String type);

    /**
     * Returns the alignment of a label with the given parameters.
     *
     * @param textBounds
     *            the label text bounds
     * @param type
     *            the label type
     * @return the label alignment
     */
    Position getAlignment(TextBounds textBounds, String type);

    /**
     * Returns the size of a label with the given parameters.
     *
     * @param textBounds
     *            the label text bounds
     * @param type
     *            the label type
     * @return the label size
     */
    Size getSize(TextBounds textBounds, String type);
}
