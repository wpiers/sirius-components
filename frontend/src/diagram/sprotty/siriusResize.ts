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
import { decorate, inject } from 'inversify';
import {
  Action,
  CommandExecutionContext,
  CommandReturn,
  Dimension,
  MergeableCommand,
  Point,
  SModelElement,
  SNode,
  TYPES,
} from 'sprotty';

export class ResizeAction implements Action {
  kind = SiriusResizeCommand.KIND;
  constructor(public readonly resize: ElementResize, public readonly finished: boolean = false) {}
}
export interface ElementResize {
  elementId: string;
  newSize: Dimension;
  newPosition: Point;
}
export class SiriusResizeCommand extends MergeableCommand {
  static readonly KIND = 'resize';

  constructor(protected readonly action: ResizeAction) {
    super();
  }

  execute(context: CommandExecutionContext): CommandReturn {
    const index = context.root.index;
    const elementResize: ElementResize = this.action.resize;
    const element: SModelElement = index.getById(elementResize.elementId);
    if (this.isNode(element)) {
      element.size = elementResize.newSize;
      element.position = elementResize.newPosition;
    }
    return context.root;
  }

  private isNode(element: SModelElement): element is SNode {
    return element instanceof SNode;
  }
  undo(context: CommandExecutionContext): CommandReturn {
    return context.root;
  }

  redo(context: CommandExecutionContext): CommandReturn {
    return context.root;
  }
}
decorate(inject(TYPES.Action), SiriusResizeCommand, 0);
