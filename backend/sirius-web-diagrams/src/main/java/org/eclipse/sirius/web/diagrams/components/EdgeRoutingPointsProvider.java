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

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.diagrams.Position;

/**
 * Provides the routing points to apply to a new edge.
 *
 * @author fbarbin
 */
public class EdgeRoutingPointsProvider {
    public List<Position> getRoutingPoints(Element source, Element target) {
        return List.of();
    }
}
