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
package org.eclipse.sirius.web.diagrams;

import java.util.Objects;
import java.util.Optional;

/**
 * Service used to compute the size of the image for a node.
 *
 * @author sbegaudeau
 * @author hmarchadour
 * @author wpiers
 */
public class ImageNodeStyleSizeProvider {

    /**
     * The default native image size.
     */
    private static final int DEFAULT_NATIVE_IMAGE_SIZE = 100;

    /**
     * Scale factor for width and height from diagram node size to bounds.
     *
     * @see org.eclipse.sirius.diagram.ui.tools.api.layout.LayoutUtils
     */
    private static final int SCALE = 10;

    private final ImageSizeProvider imageSizeProvider;

    public ImageNodeStyleSizeProvider(ImageSizeProvider imageSizeProvider) {
        this.imageSizeProvider = Objects.requireNonNull(imageSizeProvider);
    }

    public Size getSize(ImageNodeStyle imageNodeStyle) {
        String imageURL = imageNodeStyle.getImageURL();
        Optional<Size> imageSize = this.imageSizeProvider.getSize(imageURL);

        // @formatter:off
        Size defaultSize = Size.newSize()
                .height(DEFAULT_NATIVE_IMAGE_SIZE)
                .width(DEFAULT_NATIVE_IMAGE_SIZE)
                .build();
        // @formatter:on

        Size nativeSize = imageSize.orElse(defaultSize);

        Size size = null;
        int scalingFactor = imageNodeStyle.getScalingFactor();
        if (scalingFactor <= -1) {
            size = nativeSize;
        } else {
            double ratio = nativeSize.getWidth() / nativeSize.getHeight();

            int width = scalingFactor * SCALE;
            double height = width * ratio;
            // @formatter:off
            size = Size.newSize()
                    .height(height)
                    .width(width)
                    .build();
            // @formatter:on
        }
        return size;
    }
}
