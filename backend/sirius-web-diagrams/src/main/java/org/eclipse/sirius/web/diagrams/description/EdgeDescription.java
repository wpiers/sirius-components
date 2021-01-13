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
package org.eclipse.sirius.web.diagrams.description;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.diagrams.EdgeStyle;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The description of the edge.
 *
 * @author sbegaudeau
 */
@Immutable
public final class EdgeDescription {

    private UUID id;

    private SynchronizationPolicy synchronizationPolicy;

    private Function<VariableManager, String> targetObjectIdProvider;

    private Function<VariableManager, String> targetObjectKindProvider;

    private Function<VariableManager, String> targetObjectLabelProvider;

    private Function<VariableManager, List<Object>> semanticElementsProvider;

    private Optional<LabelDescription> optionalBeginLabelDescription;

    private Optional<LabelDescription> optionalCenterLabelDescription;

    private Optional<LabelDescription> optionalEndLabelDescription;

    private List<NodeDescription> sourceNodeDescriptions;

    private List<NodeDescription> targetNodeDescriptions;

    private Function<VariableManager, List<Element>> sourceNodesProvider;

    private Function<VariableManager, List<Element>> targetNodesProvider;

    private Function<VariableManager, EdgeStyle> styleProvider;

    private Function<VariableManager, Status> deleteHandler;

    private EdgeDescription() {
        // Prevent instantiation
    }

    public UUID getId() {
        return this.id;
    }

    public SynchronizationPolicy getSynchronizationPolicy() {
        return this.synchronizationPolicy;
    }

    public Function<VariableManager, String> getTargetObjectIdProvider() {
        return this.targetObjectIdProvider;
    }

    public Function<VariableManager, String> getTargetObjectKindProvider() {
        return this.targetObjectKindProvider;
    }

    public Function<VariableManager, String> getTargetObjectLabelProvider() {
        return this.targetObjectLabelProvider;
    }

    public Function<VariableManager, List<Object>> getSemanticElementsProvider() {
        return this.semanticElementsProvider;
    }

    public Optional<LabelDescription> getOptionalBeginLabelDescription() {
        return this.optionalBeginLabelDescription;
    }

    public Optional<LabelDescription> getOptionalCenterLabelDescription() {
        return this.optionalCenterLabelDescription;
    }

    public Optional<LabelDescription> getOptionalEndLabelDescription() {
        return this.optionalEndLabelDescription;
    }

    public List<NodeDescription> getSourceNodeDescriptions() {
        return this.sourceNodeDescriptions;
    }

    public List<NodeDescription> getTargetNodeDescriptions() {
        return this.targetNodeDescriptions;
    }

    public Function<VariableManager, List<Element>> getSourceNodesProvider() {
        return this.sourceNodesProvider;
    }

    public Function<VariableManager, List<Element>> getTargetNodesProvider() {
        return this.targetNodesProvider;
    }

    public Function<VariableManager, EdgeStyle> getStyleProvider() {
        return this.styleProvider;
    }

    public Function<VariableManager, Status> getDeleteHandler() {
        return this.deleteHandler;
    }

    public static Builder newEdgeDescription(UUID id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, sourceNodeDescriptionCount: {2}, targetNodeDescriptionCount: {3}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.sourceNodeDescriptions.size(), this.targetNodeDescriptions.size());
    }

    /**
     * The builder of the edge description.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private UUID id;

        private SynchronizationPolicy synchronizationPolicy = SynchronizationPolicy.SYNCHRONIZED;

        private Function<VariableManager, String> targetObjectIdProvider;

        private Function<VariableManager, String> targetObjectKindProvider;

        private Function<VariableManager, String> targetObjectLabelProvider;

        private Function<VariableManager, List<Object>> semanticElementsProvider;

        private Optional<LabelDescription> optionalBeginLabelDescription;

        private Optional<LabelDescription> optionalCenterLabelDescription;

        private Optional<LabelDescription> optionalEndLabelDescription;

        private List<NodeDescription> sourceNodeDescriptions;

        private List<NodeDescription> targetNodeDescriptions;

        private Function<VariableManager, List<Element>> sourceNodesProvider;

        private Function<VariableManager, List<Element>> targetNodesProvider;

        private Function<VariableManager, EdgeStyle> styleProvider;

        private Function<VariableManager, Status> deleteHandler;

        private Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder synchronizationPolicy(SynchronizationPolicy synchronizationPolicy) {
            this.synchronizationPolicy = synchronizationPolicy;
            return this;
        }

        public Builder targetObjectIdProvider(Function<VariableManager, String> targetObjectIdProvider) {
            this.targetObjectIdProvider = Objects.requireNonNull(targetObjectIdProvider);
            return this;
        }

        public Builder targetObjectKindProvider(Function<VariableManager, String> targetObjectKindProvider) {
            this.targetObjectKindProvider = Objects.requireNonNull(targetObjectKindProvider);
            return this;
        }

        public Builder targetObjectLabelProvider(Function<VariableManager, String> targetObjectLabelProvider) {
            this.targetObjectLabelProvider = Objects.requireNonNull(targetObjectLabelProvider);
            return this;
        }

        public Builder semanticElementsProvider(Function<VariableManager, List<Object>> semanticElementsProvider) {
            this.semanticElementsProvider = Objects.requireNonNull(semanticElementsProvider);
            return this;
        }

        public Builder optionalBeginLabelDescription(Optional<LabelDescription> optionalBeginLabelDescription) {
            this.optionalBeginLabelDescription = Objects.requireNonNull(optionalBeginLabelDescription);
            return this;
        }

        public Builder optionalCenterLabelDescription(Optional<LabelDescription> optionalCenterLabelDescription) {
            this.optionalCenterLabelDescription = Objects.requireNonNull(optionalCenterLabelDescription);
            return this;
        }

        public Builder optionalEndLabelDescription(Optional<LabelDescription> optionalEndLabelDescription) {
            this.optionalEndLabelDescription = Objects.requireNonNull(optionalEndLabelDescription);
            return this;
        }

        public Builder sourceNodeDescriptions(List<NodeDescription> sourceNodeDescriptions) {
            this.sourceNodeDescriptions = Objects.requireNonNull(sourceNodeDescriptions);
            return this;
        }

        public Builder targetNodeDescriptions(List<NodeDescription> targetNodeDescriptions) {
            this.targetNodeDescriptions = Objects.requireNonNull(targetNodeDescriptions);
            return this;
        }

        public Builder sourceNodesProvider(Function<VariableManager, List<Element>> sourceNodesProvider) {
            this.sourceNodesProvider = Objects.requireNonNull(sourceNodesProvider);
            return this;

        }

        public Builder targetNodesProvider(Function<VariableManager, List<Element>> targetNodesProvider) {
            this.targetNodesProvider = Objects.requireNonNull(targetNodesProvider);
            return this;
        }

        public Builder styleProvider(Function<VariableManager, EdgeStyle> styleProvider) {
            this.styleProvider = Objects.requireNonNull(styleProvider);
            return this;
        }

        public Builder deleteHandler(Function<VariableManager, Status> deleteHandler) {
            this.deleteHandler = Objects.requireNonNull(deleteHandler);
            return this;
        }

        public EdgeDescription build() {
            EdgeDescription edgeDescription = new EdgeDescription();
            edgeDescription.id = Objects.requireNonNull(this.id);
            edgeDescription.synchronizationPolicy = this.synchronizationPolicy;
            edgeDescription.targetObjectIdProvider = Objects.requireNonNull(this.targetObjectIdProvider);
            edgeDescription.targetObjectKindProvider = Objects.requireNonNull(this.targetObjectKindProvider);
            edgeDescription.targetObjectLabelProvider = Objects.requireNonNull(this.targetObjectLabelProvider);
            edgeDescription.sourceNodeDescriptions = Objects.requireNonNull(this.sourceNodeDescriptions);
            edgeDescription.targetNodeDescriptions = Objects.requireNonNull(this.targetNodeDescriptions);
            edgeDescription.semanticElementsProvider = Objects.requireNonNull(this.semanticElementsProvider);
            edgeDescription.optionalBeginLabelDescription = Objects.requireNonNull(this.optionalBeginLabelDescription);
            edgeDescription.optionalCenterLabelDescription = Objects.requireNonNull(this.optionalCenterLabelDescription);
            edgeDescription.optionalEndLabelDescription = Objects.requireNonNull(this.optionalEndLabelDescription);
            edgeDescription.sourceNodesProvider = Objects.requireNonNull(this.sourceNodesProvider);
            edgeDescription.targetNodesProvider = Objects.requireNonNull(this.targetNodesProvider);
            edgeDescription.styleProvider = Objects.requireNonNull(this.styleProvider);
            edgeDescription.deleteHandler = Objects.requireNonNull(this.deleteHandler);
            return edgeDescription;
        }
    }
}
