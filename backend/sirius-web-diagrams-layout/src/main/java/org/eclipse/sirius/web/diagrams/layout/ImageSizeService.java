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

import javax.annotation.PreDestroy;

import org.eclipse.sirius.web.diagrams.ImageSizeProvider;
import org.springframework.stereotype.Service;

/**
 * Service used to compute the native image size.
 *
 * @author hmarchadour
 * @author wpiers
 */
@Service
public class ImageSizeService extends ImageSizeProvider {

    @Override
    @PreDestroy
    public void dispose() {
        super.dispose();
    }

}
