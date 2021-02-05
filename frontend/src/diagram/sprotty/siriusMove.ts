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
import {
  Action,
  Dimension,
  findParentByFeature,
  isViewport,
  MoveCommand,
  MoveMouseListener,
  Point,
  ResolvedHandleMove,
  SEdge,
  SLabel,
  SModelElement,
  SNode,
  SRoutableElement,
} from 'sprotty';
import { ElementResize, ResizeAction } from './siriusResize';

export class SiriusMoveCommand extends MoveCommand {
  protected doMove(
    edge2move: Map<SRoutableElement, ResolvedHandleMove[]>,
    attachedEdgeShifts: Map<SRoutableElement, Point>
  ) {
    const allEdgesToReset = new Set<SEdge>();
    this.resolvedMoves.forEach((res) => {
      var element = res.element;
      if (isSNode(element)) {
        this.collectAllEdges(element, allEdgesToReset);
      }
      element.position = res.toPosition;
    });
    this.resetEdgesRoutingPoints(allEdgesToReset);
  }

  private resetEdgesRoutingPoints(AllEdgesToReset: Set<SEdge>) {
    AllEdgesToReset.forEach((edge) => {
      const router = this.edgeRouterRegistry!.get(edge.routerKind);
      edge.routingPoints = [];
      var routedPoints = router.route(edge);
      edge.routingPoints = routedPoints;
      this.resetLabelPosition(edge);
    });
  }

  private resetLabelPosition(edge: SEdge) {
    if (edge.routingPoints?.length === 2) {
      const label = edge.children.find((child) => this.isSLabel(child)) as SLabel;
      if (label) {
        var source = edge.routingPoints[0];
        var target = edge.routingPoints[1];
        var labelX = source.x + (target.x - source.x) / 2;
        var labelY = source.y + (target.y - source.y) / 2;
        label.position = {
          x: labelX,
          y: labelY,
        };
      }
    }
  }

  private isSLabel(element: SModelElement): element is SLabel {
    return element instanceof SLabel;
  }
  private collectAllEdges(element: SNode, allEdgesToReset: Set<SEdge>) {
    element.children.forEach((child) => {
      if (isSNode(child)) {
        this.collectAllEdges(child, allEdgesToReset);
      }
    });
    element.outgoingEdges.forEach((edge) => allEdgesToReset.add(edge));
    element.incomingEdges.forEach((edge) => allEdgesToReset.add(edge));
  }
}
const isSNode = (element: SModelElement): element is SNode => {
  return element instanceof SNode;
};
export class SiriusMoveMouseListener extends MoveMouseListener {
  intialTarget: SNode;
  startResizePosition: Point | undefined;
  startingPosition: Point;
  startingSize: Dimension;
  selector: String;
  protected snap(position: Point, element: SModelElement, isSnap: boolean): Point {
    let newPosition = super.snap(position, element, isSnap);
    if (isSNode(element)) {
      return this.getValidPosition(element, newPosition);
    }
    return newPosition;
  }

  public mouseDown(target: SModelElement, event: MouseEvent): Action[] {
    const actions: Action[] = super.mouseDown(target, event);
    if (this.startDragPosition) {
      //if the click is perfomed on a resize selector, we switch from the move mode to resize mode.
      const selector = this.isResizeSelector(event);
      if (selector) {
        if (isSNode(target)) {
          this.intialTarget = target;
          this.startingSize = target.size;
          this.startingPosition = target.position;
          this.selector = selector;
          this.startResizePosition = this.startDragPosition;
          this.startDragPosition = undefined;
        }
      }
    }
    return actions;
  }

  public mouseMove(target: SModelElement, event: MouseEvent): Action[] {
    //If we are in the resize mode we return a resize action.
    if (this.startResizePosition) {
      let result: Action[] = [];
      const action = this.getResizeAction(event, false);
      if (action) {
        result.push(action);
      }
      return result;
    } else {
      return super.mouseMove(target, event);
    }
  }
  public mouseUp(target: SModelElement, event: MouseEvent): Action[] {
    if (this.startResizePosition) {
      const result: Action[] = [];
      const action = this.getResizeAction(event, true);
      if (action) {
        result.push(action);
      }
      this.startResizePosition = undefined;
      this.intialTarget = undefined;
      this.startingPosition = undefined;
      this.startingSize = undefined;
      this.selector = undefined;
      return result;
    } else {
      return super.mouseUp(target, event);
    }
  }
  private isResizeSelector(event: MouseEvent): String | undefined {
    const domTarget = event.target as Element;
    if (domTarget?.id?.startsWith('selectorGrip_resize')) {
      return domTarget.id;
    }
    return undefined;
  }
  protected getResizeAction(event: MouseEvent, isFinished: boolean): ResizeAction | undefined {
    if (!this.startResizePosition) return undefined;
    const viewport = findParentByFeature(this.intialTarget, isViewport);
    const zoom = viewport ? viewport.zoom : 1;
    const delta = {
      x: (event.pageX - this.startResizePosition.x) / zoom,
      y: (event.pageY - this.startResizePosition.y) / zoom,
    };
    const resizeElement = this.computeElementResize(delta);
    if (resizeElement) {
      return new ResizeAction(resizeElement, isFinished);
    }
    return undefined;
  }
  private computeElementResize(delta: Point): ElementResize {
    const elementId = this.intialTarget.id;
    let previousPosition = {
      x: this.intialTarget.position.x,
      y: this.intialTarget.position.y,
    };
    let previousSize = {
      width: this.intialTarget.size.width,
      height: this.intialTarget.size.height,
    };
    if (this.selector === 'selectorGrip_resize_s') {
      [previousSize, previousPosition] = this.handleSouth(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_e') {
      [previousSize, previousPosition] = this.handleEast(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_w') {
      [previousSize, previousPosition] = this.handleWest(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_n') {
      [previousSize, previousPosition] = this.handleNorth(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_nw') {
      [previousSize, previousPosition] = this.handleNorth(previousSize, previousPosition, delta);
      [previousSize, previousPosition] = this.handleWest(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_ne') {
      [previousSize, previousPosition] = this.handleNorth(previousSize, previousPosition, delta);
      [previousSize, previousPosition] = this.handleEast(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_se') {
      [previousSize, previousPosition] = this.handleSouth(previousSize, previousPosition, delta);
      [previousSize, previousPosition] = this.handleEast(previousSize, previousPosition, delta);
    } else if (this.selector === 'selectorGrip_resize_sw') {
      [previousSize, previousPosition] = this.handleSouth(previousSize, previousPosition, delta);
      [previousSize, previousPosition] = this.handleWest(previousSize, previousPosition, delta);
    }

    return {
      elementId,
      newSize: previousSize,
      newPosition: previousPosition,
    };
  }

  private handleNorth(previousSize: Dimension, previousPosition: Point, delta: Point): [Dimension, Point] {
    const newSize = {
      width: previousSize.width,
      height: this.startingSize.height - delta.y,
    };
    const newPosition = {
      x: previousPosition.x,
      y: this.startingPosition.y + delta.y,
    };
    return [newSize, newPosition];
  }
  private handleSouth(previousSize: Dimension, previousPosition: Point, delta: Point): [Dimension, Point] {
    const newSize = {
      width: previousSize.width,
      height: this.startingSize.height + delta.y,
    };
    return [newSize, previousPosition];
  }
  private handleEast(previousSize: Dimension, previousPosition: Point, delta: Point): [Dimension, Point] {
    const newSize = {
      width: this.startingSize.width + delta.x,
      height: previousSize.height,
    };
    return [newSize, previousPosition];
  }
  private handleWest(previousSize: Dimension, previousPosition: Point, delta: Point): [Dimension, Point] {
    const newSize = {
      width: this.startingSize.width - delta.x,
      height: previousSize.height,
    };
    const newPosition = {
      x: this.startingPosition.x + delta.x,
      y: previousPosition.y,
    };
    return [newSize, newPosition];
  }
  /**
   * Provides the position within the parent bounding box.
   * @param element the element currently moved.
   * @param position the new candidate position.
   */
  private getValidPosition(element: SNode, position: Point): Point {
    const parent = element.parent;
    if (isSNode(parent)) {
      const bottomRight = {
        x: position.x + element.size.width,
        y: position.y + element.size.height,
      };
      const inBoundsBottomRight = {
        x: Math.min(bottomRight.x, parent.bounds.width),
        y: Math.min(bottomRight.y, parent.bounds.height),
      };
      const newValidPosition = {
        x: Math.max(0, inBoundsBottomRight.x - element.size.width),
        y: Math.max(0, inBoundsBottomRight.y - element.size.height),
      };
      return newValidPosition;
    }
    return position;
  }
}
