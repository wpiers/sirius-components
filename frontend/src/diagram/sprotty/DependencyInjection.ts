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
import { edgeCreationFeedback } from 'diagram/sprotty/edgeCreationFeedback';
import { GraphFactory } from 'diagram/sprotty/GraphFactory';
import siriusMoveModule from 'diagram/sprotty/siriusMoveModule';
import { DiagramView } from 'diagram/sprotty/views/DiagramView';
import { EdgeView } from 'diagram/sprotty/views/EdgeView';
import { ImageView } from 'diagram/sprotty/views/ImageView';
import { LabelView } from 'diagram/sprotty/views/LabelView';
import { RectangleView } from 'diagram/sprotty/views/RectangleView';
import {
  ACTIVE_TOOL_ACTION,
  HIDE_CONTEXTUAL_TOOLBAR_ACTION,
  SiriusWebWebSocketDiagramServer,
  SPROTTY_DELETE_ACTION,
} from 'diagram/sprotty/WebSocketDiagramServer';
import { Container, ContainerModule, decorate, inject } from 'inversify';
import {
  boundsModule,
  configureModelElement,
  configureView,
  configureViewerOptions,
  ConsoleLogger,
  defaultModule,
  edgeEditModule,
  edgeLayoutModule,
  EditLabelAction,
  editLabelFeature,
  exportModule,
  fadeModule,
  graphModule,
  hoverModule,
  HtmlRootView,
  KeyListener,
  labelEditModule,
  labelEditUiModule,
  LogLevel,
  modelSourceModule,
  MouseListener,
  overrideCommandStackOptions,
  overrideViewerOptions,
  PreRenderedView,
  RequestPopupModelAction,
  routingModule,
  SCompartmentView,
  selectModule,
  SetPopupModelAction,
  SLabel,
  SNode,
  SRoutingHandleView,
  TYPES,
  UpdateModelAction,
  updateModule,
  viewportModule,
  withEditLabelFeature,
  zorderModule,
} from 'sprotty';

const siriusWebContainerModule = new ContainerModule((bind, unbind, isBound, rebind) => {
  rebind(TYPES.ILogger).to(ConsoleLogger).inSingletonScope();
  rebind(TYPES.LogLevel).toConstantValue(LogLevel.warn);
  rebind(TYPES.IModelFactory).to(GraphFactory).inSingletonScope();
  bind(TYPES.ModelSource).to(SiriusWebWebSocketDiagramServer).inSingletonScope();

  const context = { bind, unbind, isBound, rebind };
  configureViewerOptions(context, {
    needsClientLayout: true,
    needsServerLayout: true,
  });

  // @ts-ignore
  configureView({ bind, isBound }, 'graph', DiagramView);
  // @ts-ignore
  configureModelElement(context, 'node:rectangle', SNode, RectangleView, {
    enable: [withEditLabelFeature],
  });
  // @ts-ignore
  configureModelElement(context, 'node:image', SNode, ImageView, {
    enable: [withEditLabelFeature],
  });
  // @ts-ignore
  configureView({ bind, isBound }, 'port:square', RectangleView);
  configureView({ bind, isBound }, 'edge:straight', EdgeView);
  // @ts-ignore
  configureView({ bind, isBound }, 'label:inside-left', LabelView);
  // @ts-ignore
  configureModelElement(context, 'label:inside-center', SLabel, LabelView, {
    enable: [editLabelFeature],
  });
  // @ts-ignore
  configureModelElement(context, 'label:edge-begin', SLabel, LabelView, {
    enable: [editLabelFeature],
  });
  // @ts-ignore
  configureModelElement(context, 'label:edge-center', SLabel, LabelView, {
    enable: [editLabelFeature],
  });
  // @ts-ignore
  configureModelElement(context, 'label:edge-end', SLabel, LabelView, {
    enable: [editLabelFeature],
  });
  // @ts-ignore
  configureView({ bind, isBound }, 'label:inside-right', LabelView);
  // @ts-ignore
  configureView({ bind, isBound }, 'label:outside-left', LabelView);
  // @ts-ignore
  configureView({ bind, isBound }, 'label:outside-center', LabelView);
  // @ts-ignore
  configureView({ bind, isBound }, 'label:outside-right', LabelView);
  // @ts-ignore
  configureModelElement(context, 'label:text', SLabel, LabelView, {
    enable: [editLabelFeature],
  });
  configureView({ bind, isBound }, 'comp:main', SCompartmentView);
  configureView({ bind, isBound }, 'html', HtmlRootView);
  // @ts-ignore
  configureView({ bind, isBound }, 'pre-rendered', PreRenderedView);
  configureView({ bind, isBound }, 'routing-point', SRoutingHandleView);
  configureView({ bind, isBound }, 'volatile-routing-point', SRoutingHandleView);
});

/**
 * Create the dependency injection container.
 * @param containerId The identifier of the container
 * @param onSelectElement The selection call back
 */
export const createDependencyInjectionContainer = (containerId, onSelectElement, getCursorOn, setActiveTool) => {
  const container = new Container();
  container.load(
    defaultModule,
    boundsModule,
    selectModule,
    siriusMoveModule,
    viewportModule,
    fadeModule,
    exportModule,
    hoverModule,
    graphModule,
    updateModule,
    modelSourceModule,
    routingModule,
    edgeEditModule,
    edgeLayoutModule,
    zorderModule,
    siriusWebContainerModule,
    labelEditModule,
    labelEditUiModule
  );

  const findElementWithTarget = (element) => {
    if (element.targetObjectId) {
      return element;
    } else if (element.parent) {
      return findElementWithTarget(element.parent);
    }
    // Otherwise, use the diagram as element with target.
    return element.root;
  };

  /**
   * Using our own MouseListener allows to inject the ModelSource.
   * Then, when an element is selected in the diagram, the onSelectElement()
   * method can use the ModelSource to dispatch the SPROTTY_SELECT_ACTION
   * through DiagramWebSocketContainer.
   */
  class DiagramMouseListener extends MouseListener {
    diagramServer: any;
    constructor(diagramServer) {
      super();
      this.diagramServer = diagramServer;
    }

    mouseDown(element, event) {
      if (event.button === 0) {
        const elementWithTarget = findElementWithTarget(element);
        onSelectElement(elementWithTarget, this.diagramServer);
      } else if (event.button === 2) {
        edgeCreationFeedback.reset();
        setActiveTool();
        return [{ kind: ACTIVE_TOOL_ACTION, tool: undefined }];
      }
      return [];
    }
    mouseMove(target, event) {
      edgeCreationFeedback.update(event.offsetX, event.offsetY);
      return [];
    }
  }
  decorate(inject(TYPES.ModelSource), DiagramMouseListener, 0);

  container.bind(TYPES.MouseListener).to(DiagramMouseListener).inSingletonScope();

  class CursorMouseListener extends MouseListener {
    diagramServer: any;
    constructor(diagramServer) {
      super();
      this.diagramServer = diagramServer;
    }

    mouseMove(element, event) {
      const root = element.root;
      const elementWithTarget = findElementWithTarget(element);
      const expectedCursor = getCursorOn(elementWithTarget, this.diagramServer);
      if (root.cursor !== expectedCursor) {
        root.cursor = expectedCursor;
        return [
          {
            kind: UpdateModelAction.KIND,
            input: root,
          },
        ];
      }

      return [];
    }
  }
  decorate(inject(TYPES.ModelSource), CursorMouseListener, 0);

  container.bind(TYPES.MouseListener).to(CursorMouseListener).inSingletonScope();

  // The list of characters that will enable the direct edit mechanism.
  const directEditActivationValidCharacters = /[\w&é§èàùçÔØÁÛÊË"«»’”„´$¥€£\\¿?!=+-,;:%/{}[\]–#@*.]/;

  const keyListener = new KeyListener();
  keyListener.keyDown = (element, event) => {
    if (event.code === 'Delete') {
      return [{ kind: SPROTTY_DELETE_ACTION, element }];
    } else if (event.code === 'F2') {
      return [{ kind: EditLabelAction.KIND, element }];
    } else if (event.code === 'Escape') {
      return [{ kind: HIDE_CONTEXTUAL_TOOLBAR_ACTION }];
    } else {
      /*If a modifier key is hit alone, do nothing*/
      if ((event.altKey || event.shiftKey) && event.getModifierState(event.key)) {
        return [];
      }
      const validFirstInputChar =
        !event.metaKey &&
        !event.ctrlKey &&
        event.key.length === 1 &&
        directEditActivationValidCharacters.test(event.key);
      if (validFirstInputChar) {
        return [{ kind: EditLabelAction.KIND, element }];
      }
    }
    return [];
  };
  container.bind(TYPES.KeyListener).toConstantValue(keyListener);

  overrideViewerOptions(container, {
    baseDiv: containerId,
    hiddenDiv: containerId + '-hidden',
  });
  overrideCommandStackOptions(container, {
    defaultDuration: 500,
  });
  /**
   * Workaround to disable Sprotty standard popup feature.
   * We handle the contextual toolbar on selection (not on hover).
   */
  const actionDispatcher = container.get(TYPES.IActionDispatcher) as any;
  const dispatchFn = actionDispatcher.dispatch;
  // Override the dispatch function to ignore Sprotty SetPopupModelAction & RequestPopupModelAction.
  actionDispatcher.dispatch = (action) => {
    if (
      !action.fromSirius &&
      (action.kind === SetPopupModelAction.KIND || action.kind === RequestPopupModelAction.KIND)
    ) {
      return undefined;
    }
    return dispatchFn.call(actionDispatcher, action);
  };
  return container;
};
