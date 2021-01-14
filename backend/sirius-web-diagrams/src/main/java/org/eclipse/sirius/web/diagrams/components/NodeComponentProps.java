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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.diagrams.Node;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.ViewCreationRequest;
import org.eclipse.sirius.web.diagrams.description.NodeDescription;
import org.eclipse.sirius.web.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The properties of the node component.
 *
 * @author sbegaudeau
 */
@Immutable
public final class NodeComponentProps implements IProps {

    private VariableManager variableManager;

    private NodeDescription nodeDescription;

    private INodesRequestor nodesRequestor;

    private NodeContainmentKind containmentKind;

    private DiagramRenderingCache cache;

    private List<ViewCreationRequest> viewCreationRequests;

    private UUID parentElementId;

    private NodePositionProvider nodePositionProvider;

    private Optional<Node> parentNode;

    private Optional<Position> optionalParentAbsolutePosition;

    private NodeComponentProps() {
        // Prevent instantiation
    }

    public static Builder newNodeComponentProps() {
        return new Builder();
    }

    public VariableManager getVariableManager() {
        return this.variableManager;
    }

    public NodeDescription getNodeDescription() {
        return this.nodeDescription;
    }

    public INodesRequestor getNodesRequestor() {
        return this.nodesRequestor;
    }

    public NodeContainmentKind getContainmentKind() {
        return this.containmentKind;
    }

    public DiagramRenderingCache getCache() {
        return this.cache;
    }

    public List<ViewCreationRequest> getViewCreationRequests() {
        return this.viewCreationRequests;
    }

    public UUID getParentElementId() {
        return this.parentElementId;
    }

    public NodePositionProvider getNodePositionProvider() {
        return this.nodePositionProvider;
    }

    public Optional<Node> getParentNode() {
        return this.parentNode;
    }

    public Optional<Position> getOptionalParentAbsolutePosition() {
        return this.optionalParentAbsolutePosition;
    }

    /**
     * The Builder to create a new {@link NodeComponentProps}.
     *
     * @author fbarbin
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private VariableManager variableManager;

        private NodeDescription nodeDescription;

        private INodesRequestor nodesRequestor;

        private NodeContainmentKind containmentKind;

        private DiagramRenderingCache cache;

        private List<ViewCreationRequest> viewCreationRequests;

        private UUID parentElementId;

        private NodePositionProvider nodePositionProvider;

        private Optional<Node> parentNode;

        private Optional<Position> optionalParentAbsolutePosition;

        public Builder variableManager(VariableManager variableManager) {
            this.variableManager = Objects.requireNonNull(variableManager);
            return this;
        }

        public Builder nodeDescription(NodeDescription nodeDescription) {
            this.nodeDescription = Objects.requireNonNull(nodeDescription);
            return this;
        }

        public Builder nodesRequestor(INodesRequestor nodesRequestor) {
            this.nodesRequestor = Objects.requireNonNull(nodesRequestor);
            return this;
        }

        public Builder containmentKind(NodeContainmentKind containmentKind) {
            this.containmentKind = Objects.requireNonNull(containmentKind);
            return this;
        }

        public Builder cache(DiagramRenderingCache cache) {
            this.cache = Objects.requireNonNull(cache);
            return this;
        }

        public Builder viewCreationRequests(List<ViewCreationRequest> viewCreationRequests) {
            this.viewCreationRequests = Objects.requireNonNull(viewCreationRequests);
            return this;
        }

        public Builder parentElementId(UUID parentElementId) {
            this.parentElementId = Objects.requireNonNull(parentElementId);
            return this;
        }

        public Builder nodePositionProvider(NodePositionProvider nodePositionProvider) {
            this.nodePositionProvider = Objects.requireNonNull(nodePositionProvider);
            return this;
        }

        public Builder parentNode(Optional<Node> parentNode) {
            this.parentNode = Objects.requireNonNull(parentNode);
            return this;
        }

        public Builder optionalParentAbsolutePosition(Optional<Position> optionalParentAbsolutePosition) {
            this.optionalParentAbsolutePosition = Objects.requireNonNull(optionalParentAbsolutePosition);
            return this;
        }

        NodeComponentProps build() {
            NodeComponentProps nodeComponentProps = new NodeComponentProps();
            nodeComponentProps.variableManager = Objects.requireNonNull(this.variableManager);
            nodeComponentProps.nodeDescription = Objects.requireNonNull(this.nodeDescription);
            nodeComponentProps.nodesRequestor = Objects.requireNonNull(this.nodesRequestor);
            nodeComponentProps.containmentKind = Objects.requireNonNull(this.containmentKind);
            nodeComponentProps.cache = Objects.requireNonNull(this.cache);
            nodeComponentProps.viewCreationRequests = Objects.requireNonNull(this.viewCreationRequests);
            nodeComponentProps.parentElementId = Objects.requireNonNull(this.parentElementId);
            nodeComponentProps.nodePositionProvider = Objects.requireNonNull(this.nodePositionProvider);
            nodeComponentProps.parentNode = Objects.requireNonNull(this.parentNode);
            nodeComponentProps.optionalParentAbsolutePosition = Objects.requireNonNull(this.optionalParentAbsolutePosition);
            return nodeComponentProps;
        }
    }

}
