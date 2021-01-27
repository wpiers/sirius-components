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
package org.eclipse.sirius.web.collaborative.diagrams.api.dto;

import java.text.MessageFormat;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLInputObjectType;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramInput;

/**
 * The input for the "Invoke a node tool on diagram" mutation.
 *
 * @author pcdavid
 */
@GraphQLInputObjectType
public final class InvokeNodeToolOnDiagramInput implements IDiagramInput {
    private UUID projectId;

    private UUID representationId;

    private UUID diagramElementId;

    private String toolId;

    private double startingPositionX;

    private double startingPositionY;

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getProjectId() {
        return this.projectId;
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getRepresentationId() {
        return this.representationId;
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getDiagramElementId() {
        return this.diagramElementId;
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public String getToolId() {
        return this.toolId;
    }

    @GraphQLField
    @GraphQLNonNull
    public double getStartingPositionX() {
        return this.startingPositionX;
    }

    @GraphQLField
    @GraphQLNonNull
    public double getStartingPositionY() {
        return this.startingPositionY;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'projectId: {1}, representationId: {2}, diagramElementId: {3}, toolId: {4}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.projectId, this.representationId, this.diagramElementId, this.toolId);
    }

}
