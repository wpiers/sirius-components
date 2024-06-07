/*******************************************************************************
 * Copyright (c) 2019, 2024 Obeo.
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
import { WorkbenchViewComponentProps } from '@eclipse-sirius/sirius-components-core';
import { makeStyles } from '@material-ui/core/styles';
import { useEffect, useRef, useState } from 'react';
import { TreeToolBar } from '../toolbar/TreeToolBar';
import { FilterBar } from '../trees/FilterBar';
import { ExplorerViewState, TreeFilter } from './ExplorerView.types';
import { useExplorerViewConfiguration } from './ExplorerViewConfiguration';
import { TreeView } from './TreeView';
import { useTreeFilters } from './useTreeFilters';

const useStyles = makeStyles((theme) => ({
  treeView: {
    display: 'flex',
    flexDirection: 'column',
  },
  treeContent: {
    paddingTop: theme.spacing(1),
    flexGrow: 1,
    overflow: 'auto',
  },
}));

const explorerTreeId = 'explorer://';

export const ExplorerView = ({ editingContextId, readOnly }: WorkbenchViewComponentProps) => {
  const styles = useStyles();
  const { converter } = useExplorerViewConfiguration();

  const initialState: ExplorerViewState = {
    synchronizedWithSelection: true,
    filterBar: false,
    filterBarText: '',
    filterBarTreeFiltering: false,
    treeFilters: [],
  };
  const [state, setState] = useState<ExplorerViewState>(initialState);

  const { loading, treeFilters } = useTreeFilters(editingContextId, 'explorer://');

  useEffect(() => {
    if (!loading) {
      const allFilters: TreeFilter[] = treeFilters.map((gqlTreeFilter) => ({
        id: gqlTreeFilter.id,
        label: gqlTreeFilter.label,
        state: gqlTreeFilter.defaultState,
      }));
      setState((prevState) => ({ ...prevState, treeFilters: allFilters }));
    }
  }, [loading, treeFilters]);

  const treeElement = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const downHandler = (event) => {
      if ((event.ctrlKey === true || event.metaKey === true) && event.key === 'f' && event.target.tagName !== 'INPUT') {
        event.preventDefault();
        setState((prevState) => {
          return { ...prevState, filterBar: true, filterBarText: '', filterBarTreeFiltering: false };
        });
      }
    };
    const element = treeElement?.current;
    if (element) {
      element.addEventListener('keydown', downHandler);

      return () => {
        element.removeEventListener('keydown', downHandler);
      };
    }
    return null;
  }, [treeElement]);

  let filterBar: JSX.Element;
  if (state.filterBar) {
    filterBar = (
      <FilterBar
        onTextChange={(event) => {
          const {
            target: { value },
          } = event;
          setState((prevState) => {
            return { ...prevState, filterBarText: value };
          });
        }}
        onFilterButtonClick={(enabled) =>
          setState((prevState) => {
            return { ...prevState, filterBarTreeFiltering: enabled };
          })
        }
        onClose={() =>
          setState((prevState) => {
            return { ...prevState, filterBar: false, filterBarText: '', filterBarTreeFiltering: false };
          })
        }
      />
    );
  }

  const activeTreeFilterIds = state.treeFilters.filter((filter) => filter.state).map((filter) => filter.id);

  return (
    <div className={styles.treeView} ref={treeElement}>
      <TreeToolBar
        editingContextId={editingContextId}
        treeId={explorerTreeId}
        readOnly={readOnly}
        onSynchronizedClick={() =>
          setState((prevState) => {
            return { ...prevState, synchronizedWithSelection: !state.synchronizedWithSelection };
          })
        }
        synchronized={state.synchronizedWithSelection}
        treeFilters={state.treeFilters}
        onTreeFilterMenuItemClick={(treeFilters) =>
          setState((prevState) => {
            return { ...prevState, treeFilters };
          })
        }
      />
      <div className={styles.treeContent}>
        {filterBar}
        <TreeView
          editingContextId={editingContextId}
          readOnly={readOnly}
          treeId={explorerTreeId}
          enableMultiSelection={true}
          synchronizedWithSelection={state.synchronizedWithSelection}
          activeFilterIds={activeTreeFilterIds}
          textToHighlight={state.filterBarText}
          textToFilter={state.filterBarTreeFiltering ? state.filterBarText : null}
          converter={converter}
        />
      </div>
    </div>
  );
};
