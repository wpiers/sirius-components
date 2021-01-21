/*******************************************************************************
 * Copyright (c) 2021 Obeo.
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
package org.eclipse.sirius.web.diagrams.layout;

import org.eclipse.sirius.web.diagrams.ImageNodeStyleSizeProvider;
import org.springframework.stereotype.Service;

/**
 * Service used to compute the size of the image for a node.
 *
 * @author sbegaudeau
 * @author hmarchadour
 * @author wpiers
 */
@Service
public class ImageNodeStyleSizeService extends ImageNodeStyleSizeProvider {

    public ImageNodeStyleSizeService(ImageSizeService imageSizeService) {
        super(imageSizeService);
    }

}
