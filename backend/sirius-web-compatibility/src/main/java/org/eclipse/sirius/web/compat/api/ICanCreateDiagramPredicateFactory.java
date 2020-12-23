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
package org.eclipse.sirius.web.compat.api;

import java.util.function.Predicate;

import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.web.interpreter.AQLInterpreter;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Used to provide a predicate to determine if we can create instances of the Sirius RCP diagram description.
 *
 * @author sbegaudeau
 */
public interface ICanCreateDiagramPredicateFactory {
    Predicate<VariableManager> getCanCreateDiagramPredicate(DiagramDescription diagramDescription, AQLInterpreter interpreter);
}
