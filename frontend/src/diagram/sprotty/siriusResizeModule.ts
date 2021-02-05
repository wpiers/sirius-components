/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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

import { ContainerModule } from 'inversify';
import { configureCommand } from 'sprotty';
import { SiriusResizeCommand } from './siriusResize';

const siriusResizeModule = new ContainerModule((bind, _unbind, isBound) => {
  configureCommand({ bind, isBound }, SiriusResizeCommand);
});

export default siriusResizeModule;
