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
package org.eclipse.sirius.web.emf.compatibility.diagrams;

import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.web.emf.compatibility.DomainClassPredicate;
import org.eclipse.sirius.web.interpreter.AQLInterpreter;
import org.eclipse.sirius.web.interpreter.Result;
import org.eclipse.sirius.web.representations.IRepresentationDescription;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Predicate to test the ability to create a diagram according to the given {@Link VariableManager}.
 *
 * @author hmarchadour
 */
public class CanCreateDiagramPredicate implements Predicate<VariableManager> {

    private final DiagramDescription diagramDescription;

    private final AQLInterpreter interpreter;

    public CanCreateDiagramPredicate(DiagramDescription diagramDescription, AQLInterpreter interpreter) {
        this.diagramDescription = diagramDescription;
        this.interpreter = interpreter;
    }

    @Override
    public boolean test(VariableManager variableManager) {
        boolean result = false;

        String domainClass = this.diagramDescription.getDomainClass();

        // @formatter:off
        Optional<EObject> optionalEObject = Optional.ofNullable(variableManager.getVariables().get(IRepresentationDescription.CLASS))
                .filter(EClass.class::isInstance)
                .map(EClass.class::cast)
                .map(EcoreUtil::create)
                .filter(new DomainClassPredicate(domainClass));
        // @formatter:on

        if (optionalEObject.isPresent()) {
            String preconditionExpression = this.diagramDescription.getPreconditionExpression();
            if (preconditionExpression != null && !preconditionExpression.isBlank()) {
                Result preconditionResult = this.interpreter.evaluateExpression(variableManager.getVariables(), preconditionExpression);
                result = preconditionResult.asBoolean().orElse(false);
            } else {
                result = true;
            }
        }

        return result;
    }

}
