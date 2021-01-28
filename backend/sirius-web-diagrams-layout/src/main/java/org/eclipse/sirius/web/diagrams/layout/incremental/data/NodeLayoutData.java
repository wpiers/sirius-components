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
package org.eclipse.sirius.web.diagrams.layout.incremental.data;

import java.util.List;

import org.eclipse.sirius.web.diagrams.INodeStyle;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;

/**
 * A mutable structure to store/update the nodes layout.
 *
 * @author wpiers
 */
public class NodeLayoutData implements IContainerLayoutData, IConnectable {

    private String id;

    private Position position;

    private Size size;

    private IContainerLayoutData parent;

    private List<NodeLayoutData> borderNodes;

    private List<NodeLayoutData> childNodes;

    private LabelLayoutData label;

    private String nodeType;

    private INodeStyle style;

    private boolean changed;

    private boolean pinned;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Size getSize() {
        return this.size;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    public IContainerLayoutData getParent() {
        return this.parent;
    }

    public void setParent(IContainerLayoutData parent) {
        this.parent = parent;
    }

    public List<NodeLayoutData> getBorderNodes() {
        return this.borderNodes;
    }

    public void setBorderNodes(List<NodeLayoutData> borderNodes) {
        this.borderNodes = borderNodes;
    }

    @Override
    public List<NodeLayoutData> getChildNodes() {
        return this.childNodes;
    }

    @Override
    public void setChildNodes(List<NodeLayoutData> childNodes) {
        this.childNodes = childNodes;
    }

    public LabelLayoutData getLabel() {
        return this.label;
    }

    public void setLabel(LabelLayoutData label) {
        this.label = label;
    }

    public INodeStyle getStyle() {
        return this.style;
    }

    public void setStyle(INodeStyle style) {
        this.style = style;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public boolean hasChanged() {
        return this.changed;
    }

    @Override
    public void setChanged(boolean hasChanged) {
        this.changed = hasChanged;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
