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
import org.eclipse.sirius.web.diagrams.Size;

/**
 * Provides the bounds (Size and Position) to apply to a new Edge Label.
 *
 * @author fbarbin
 */
public class EdgeLabelBoundsProvider {

    public Position getPosition() {
        return Position.UNDEFINED;
    }

    public Position getAlignment() {
        return Position.UNDEFINED;
    }

    public Size getSize() {
        return Size.UNDEFINED;
    }

}
