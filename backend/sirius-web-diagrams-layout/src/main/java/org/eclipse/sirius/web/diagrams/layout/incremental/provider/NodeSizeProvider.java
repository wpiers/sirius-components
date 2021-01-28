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
package org.eclipse.sirius.web.diagrams.layout.incremental.provider;

import org.eclipse.sirius.web.diagrams.INodeStyle;
import org.eclipse.sirius.web.diagrams.ImageNodeStyle;
import org.eclipse.sirius.web.diagrams.ImageNodeStyleSizeProvider;
import org.eclipse.sirius.web.diagrams.ImageSizeProvider;
import org.eclipse.sirius.web.diagrams.Size;

/**
 * Provides the minimal size of a Node.
 *
 * @author wpiers
 */
public class NodeSizeProvider {

    public Size getSize(INodeStyle style) {
        if (style instanceof ImageNodeStyle) {
            return new ImageNodeStyleSizeProvider(new ImageSizeProvider()).getSize((ImageNodeStyle) style);
        }
        // @formatter:off
        return Size.newSize()
                .width(150)
                .height(70)
                .build();
        // @formatter:on
    }

}
