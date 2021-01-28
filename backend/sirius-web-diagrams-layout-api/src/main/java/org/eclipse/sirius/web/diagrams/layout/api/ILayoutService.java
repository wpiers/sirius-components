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
package org.eclipse.sirius.web.diagrams.layout.api;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Position;

/**
 * Implementation of this interface will layout the given diagram.
 *
 * @author sbegaudeau
 */
public interface ILayoutService {
    Diagram layout(Diagram diagram);

    Diagram incrementalLayout(Diagram newDiagram, Map<UUID, Position> movedElementIDToNewPositionMap, Optional<Position> optionalStartingPosition);
}
